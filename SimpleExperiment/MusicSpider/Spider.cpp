#include "stdafx.h"
#define _WINSOCK_DEPRECATED_NO_WARNINGS
#define _CRT_SECURE_NO_WARNINGS
#undef  UNICODE
#include <WinSock2.h>
#include <Windows.h>
#include <stdlib.h>
#include <stdio.h>
#include <assert.h>
#include <Shlwapi.h>
#include <map> 
#include <set> 
#include<stack>
#include<list>
#include "./utils.hxx"

using namespace std;

#pragma warning(disable:4996 4998)
#pragma comment(lib,"ws2_32")
#pragma comment(lib,"Shlwapi")

#define MAX_SPIDER_NUMBER    8

volatile LONG			g_iSpider = 0; // the total number of spiders which are running now
CRITICAL_SECTION		g_hSyn;
DBLOB					g_htmlFile;
set<URL_HELPER>			tableUrl;
set<LONG64>				tableSong;
list<URL_HELPER*>       g_quePending;
HANDLE                  g_hSemaphore;
LONG volatile           g_maxUrlNumber = 1024;
LONG                    g_iUrlNumber = 0;
HANDLE                  g_hSignal;
BOOL                    g_bTrace = FALSE;
LONG                    g_fi = 0;
LONG volatile           g_bClosed = FALSE;
LONG volatile           g_bOnline = TRUE;
LONG					m_number = 0;

void url_spider(PURL_HELPER u, IN BOOL bImportant);
static void music_filter(char *pszToken);
static void _url_spider(IN PURL_HELPER u, IN BOOL isSong);
/*合成完整url*/
static URL_HELPER* fixUrl(IN URL_HELPER *par, IN const char *pszUrl) {

	if (pszUrl[0] == '#' ||
		pszUrl[0] == ' ' ||
		pszUrl[0] == '\0' ||
		0 == ::_strnicmp(pszUrl, "javascript", 10) ||
		0 == ::_strnicmp(pszUrl, "mailto", 6) ||
		0 == ::_strnicmp(pszUrl, "data:", 5) ||
		0 == ::_strnicmp(pszUrl, "ftp:", 4) ||
		0 == ::_strnicmp(pszUrl, "https:", 6) ||
		0 == ::_strnicmp(pszUrl, "file:", 5) ||
		0 == ::_strnicmp(pszUrl, "about:", 6) ||
		0 == ::_strnicmp(pszUrl, "tel:", 4) ||
		0 == ::_strnicmp(pszUrl, "/song?id=", 8) ||
		0 == ::_strnicmp(pszUrl, "/artist?id=", 11) ||
		0 == ::_strnicmp(pszUrl, "/album?id=", 10)
		)
	{
		//
		// It's not a real & valid url,and just ignore it as quickly as possible
		//
		return NULL;
	}
	DWORD i = 1024 * 8;
	DBLOB foo; foo.GrowBuffer(i);

	HRESULT hr = UrlCombineA(*par, pszUrl, foo.GetPointer<PSTR>(0), &i, 0);
	if (hr != S_OK) {
		xPANIC("Invalid url:[%s]\r\n", pszUrl);
		return NULL;
	}
	foo.IncSize((INT)i);
	if (0 != ::_strnicmp(foo, "http://", 7)) {
		xPANIC("Invalid url:[%s]\r\n", pszUrl);
		return NULL;
	}
	URL_HELPER *u = new URL_HELPER(foo);
	if (u->IsValid()) {// && par->checkDomain(*u)) {
		return u;
	}
	delete u;
	return NULL;
}

SOCKET conn2Peer(ULONG ipv, USHORT PortNumber) {
	//
	// create a socket handle (IPv4, TCP only)
	//
	SOCKET       fd = socket(AF_INET, SOCK_STREAM, 0);
	SOCKADDR_IN  addr;
	int          i;
	//
	// Fill up local address & port information into addr structure
	//
	addr.sin_family = AF_INET;   // IPv4 
	addr.sin_port = 0;         // zero means any idle port
	addr.sin_addr.S_un.S_addr = 0;         // zero means any local available IPv4 address
										   //
										   // bind this socket on this address & port
										   //
	i = bind(fd, (const sockaddr*)&addr, sizeof(SOCKADDR_IN));
	ASSERT(i != SOCKET_ERROR);
	//
	// Fill up our peer address & port inforamtion
	//
	addr.sin_addr.S_un.S_addr = ipv;               // 32-bit IPv4 address, Actually it's a unsigned long integer
	addr.sin_port = htons(PortNumber); // host-order to network order, please!
									   //	addr.sin_addr.S_un.S_addr = inet_addr("127.0.0.1");
									   //	addr.sin_port = htons(8888);
									   //
									   // try to make connection to our peer (TCP 3-way handshakes)
									   //
	ULONG opt = 19701224; // The day I got my life 
	::ioctlsocket(fd, FIONBIO, &opt);
	i = connect(fd, (const sockaddr*)&addr, sizeof(SOCKADDR_IN));
	if (i != NOERROR) {
		FD_SET foo;
		timeval tval = { 10,0 };
		FD_ZERO(&foo);
		FD_SET(fd, &foo);
		i = select(1, NULL, &foo, NULL, &tval);
		if (i == 1) {
			if (FD_ISSET(fd, &foo)) {
				i = NOERROR;
			}

		}
		else {
			i = SOCKET_ERROR;
		}
	}
	if (i != NOERROR) {
		//
		// Oops, we can't make a connection to this endpoint! 
		//
		//	xPANIC("connect failed:%d", WSAGetLastError());
		closesocket(fd);
		//
		// Failure !
		//
		fd = SOCKET_ERROR;
	}
	DWORD rtimer = 1000 * 10;
	opt = 0;
	::ioctlsocket(fd, FIONBIO, &opt);
	::setsockopt(fd, SOL_SOCKET, SO_RCVTIMEO, (const char*)&rtimer, sizeof(DWORD));
	return fd;
}
SOCKET conn2Peer(const char *pszHost, USHORT PortNumber) {
	//
	// Call this function to convert pszHost into a legal IPv4 address.i.e. DNS resolving
	//
	hostent *p = gethostbyname(pszHost);
	if (!p) {
		//
		// Failed to resolve this domain name, and we can do nothing more !
		//
		xPANIC("Failed to resolve [%s] error number:%d\r\n", pszHost, WSAGetLastError());
		return SOCKET_ERROR;
	}
	//
	// ipv is what we want to get!
	//
	ULONG ipv = *(ULONG*)p->h_addr_list[0];
	//printf("\r\n%d\r\n",ipv);
	//
	// See the above function 
	//
	return conn2Peer(ipv, PortNumber);
}

BOOL GetHeader(DBLOB &headers, const char *pszName, DBLOB &value) {
	char *Ptr = headers.GetPointer<char*>(0);
	char *Tail = headers.GetTailPointer<char*>();
	int  len = (int)strlen(pszName);
	while (Ptr < Tail) {
		if (0 == ::_strnicmp(Ptr, pszName, len) && Ptr[len] == ':') {
			Ptr += len;
			while (Ptr < Tail && strchr(": \t", *Ptr)) {
				Ptr++;
			}
			char *Prev = Ptr;
			while (Ptr < Tail && !strchr("\r\n", *Ptr)) {
				Ptr++;
			}
			value.PutIntoBlob(Prev, PTR_DIFF(Ptr, Prev));
			return true;
		}
		while (Ptr < Tail && '\n' != *Ptr++) {}
	}
	return false;
}
/*分析a标签*/
static void tag_a_filter(PURL_HELPER url, char *pszToken, BOOL isSong) {
	//
	// <a href="http:/www.abc.com/xxx.html"> Welcome to </a>
	//http://music.163.com/song/media/outer/url?id=65533
	//<a href="/song?id=557581838"><b title="开口 - (电视剧《归去来》人物主题曲&nbsp;)">开<div class="soil">爜缴紅遞</div>口</b></a>
	//<a class="" href="/artist?id=4755" hidefocus="true">品<div class="soil">大燖時鳲</div>冠</a>
	//<a href="/album?id=38591213" title="归去来&nbsp;电视原声带">归<div class="soil">睰</div>去来&nbsp;电视原声带</a>
	//https://music.163.com/

	int  len = (int)strlen(pszToken);
	char *Ptr = pszToken;
	char *Tail = &Ptr[len];
	while (Ptr < Tail) {
		if (0 == ::_strnicmp(Ptr, "href=", 5)) {
			Ptr += 5;
			if (*Ptr == '\"' || *Ptr == '\'') {
				char *Prev = ++Ptr;
				while (Ptr < Tail && !strchr("\"\' \t<>", *Ptr)) {
					Ptr++;
				}
				char chr = *Ptr;
				*Ptr = '\0';	//prev是href内容
				URL_HELPER *u = fixUrl(url, Prev);
				if (u) {
					url_spider(u, FALSE);//进队列
				}
				else {
					if (0 == ::_strnicmp(Prev, "/song?id=", 9)) {//网易
						Prev += 9;
						if (*Prev >= '0'&&*Prev <= '9') {
							music_filter(Prev);
						}
					}
				}
				*Ptr = chr;
			}
			break;
		}
		Ptr++;
	}
	return;
}

static void music_filter(char *pszToken) {
	char *a = new char[100];
	char *b = new char[100];
	//做快速测试用（大概70几首歌）
	//int num = int(pszToken);
	//下面是歌曲比较多的形式，大概三四千首
	int num = atoi(pszToken);
	strcpy(a, "http://music.163.com/song/media/outer/url?id=");
	strcat(a, pszToken);
	strcpy(b, "http://music.163.com/song?id=");
	strcat(b, pszToken);
	URL_HELPER *u = new URL_HELPER(b);
	::EnterCriticalSection(&g_hSyn);
	if (tableSong.count(num) == 0) {
		//xTRACE("%d,\r\n", num);
		tableSong.insert(num);
		m_number++;
		xTRACE("\r\n%d：{songurl:\"%s\",\r\n", m_number, a);
		g_htmlFile.Printf("{songurl:\"%s\",\r\n", a);
		if (tableUrl.count(*u) == 0) {
			tableUrl.insert(*u);
		}
		_url_spider(u, TRUE);
	}
	::LeaveCriticalSection(&g_hSyn);
	delete(a);
	delete(b);
}
static void tag_img_filter(PURL_HELPER url, char *pszToken, BOOL isSong) {
	//
	// <img src="http://www.abc.com/img/abc.jpg" />
	//
	int  len = (int)strlen(pszToken);
	char *Ptr = pszToken;
	char *Tail = &Ptr[len];
	char *Prev = Ptr;
	boolean jud = true;
	while (Ptr < Tail) {
		if (0 == ::_strnicmp(Ptr, "src", 3)) {
			Ptr += 4;
			if (*Ptr == '\"' || *Ptr == '\'') {
				Prev = ++Ptr;
				while (Ptr < Tail && !strchr("\"\'", *Ptr)) {
					if (0 == ::_strnicmp(Ptr, "\r\n", 2)) {
						jud = false;
						break;
					}
					Ptr++;
				}
			}
			break;
		}
		Ptr++;
	}
	char chr = *Ptr;
	*Ptr = '\0';
	if (jud) {
		if (!isSong)
			::EnterCriticalSection(&g_hSyn);
		xTRACE("img:\"%s\",\r\n", Prev);
		g_htmlFile.Printf("img:\"%s\",\r\n", Prev);
		if (!isSong)
			::LeaveCriticalSection(&g_hSyn);
	}
	else {//img标签异常（原因不知道，会出现换行）
		xTRACE("img: "",\r\n");
		g_htmlFile.Printf("img: \"\",\r\n");
	}
	*Ptr = chr;
	return;
}

/**************************************************************
* url_filter
*
*/
static void url_filter(PURL_HELPER url, DBLOB &header, DBLOB &body, BOOL isSong) {

	if (header.GetSize() > 0) {
		DBLOB blob;
		if (GetHeader(header, "Location", blob) && !isSong) {	//拓展
			URL_HELPER *u = new URL_HELPER(blob);
			if (u->IsValid()) {// && url->checkDomain(*u)) {
				xTRACE("Redirect:[%s] -> [%s]\r\n", (char*)*url, (char*)*u);
				url_spider(u, FALSE);
			}
			else {
				delete u;
			}
			return;
		}
		blob.Cleanup();
		if (!isSong) {
			if (!GetHeader(header, "Content-Type", blob)) {
				xTRACE("\r\nThere should be a valid Content-Type header value\r\n%s\r\n", (const char*)header);
				return;
			}
			if (0 == ::_strnicmp(blob, "image/", 6)) {
				//image_filter(url, header, body);
				return;
			}
			if (0 == ::_strnicmp(blob, "audio/", 6)) {
				//audio_filter(url, header, body);
				return;
			}
			if (0 == ::_strnicmp(blob, "video/", 6)) {
				//video_filter(url, header, body);
				return;
			}
			if (0 != ::_strnicmp(blob, "text/", 5)) {
				//
				// It's not a textual message
				//
				return;
			}
		}
		if (body.GetSize() > 0) {
			char   *Ptr = body.GetPointer<char*>();
			char   *Tail = body.GetTailPointer<char*>();
			char   *Prev = Ptr;
			if (isSong) {//分析歌曲页<div class="f-cb">
				while (Ptr < Tail) {
					if ('<' == *Ptr++ && '/' != *Ptr) {
						Prev = Ptr;
						if ((0 == ::_strnicmp(Prev, "div class=\"f", 12))) {
							//<img src="http://p1.music.126.net/EcV9ArRh2VIIQJZT7ESlqA==/109951163313831988.jpg"图片
							while (Ptr < Tail) {
								if (0 == ::_strnicmp(Ptr, "<img", 4))
									break;
								Ptr++;
							}
							tag_img_filter(url, Ptr, isSong);
							//<div class="tit"><em class="f-ff2">良药</em>歌名
							while (0 != ::_strnicmp(Ptr, "<em", 3) && Ptr++ < Tail);
							while ('>' != *Ptr && Ptr++ < Tail);
							Prev = ++Ptr;
							while (Ptr < Tail) {
								if ('<' == *Ptr || 0 == ::_strnicmp(Ptr, "\r\n", 2))
									break;
								Ptr++;
							}
							char chr = *Ptr;
							*Ptr = '\0';
							xTRACE("songName:\"%s\",\r\n", Prev);
							g_htmlFile.Printf("songName:\"%s\",\r\n", Prev);
							*Ptr = chr;
							break;
						}
					}
				}
			}
			int i = 0;
			char tab[2][50] = { "artist","album" };
			if (isSong&&Ptr >= Tail)//防意外情况
				g_htmlFile.Printf("artist:\"\",\r\nalbum:\"\"\r\n},\r\n");
			while (Ptr < Tail) {
				//<a class="s-fc7" href="/artist?id=5781">薛之谦</a>
				//      token                             tmp
				if ('<' == *Ptr++ && '/' != *Ptr) {
					Prev = Ptr;
					if (0 != ::_strnicmp(Prev, "a ", 2)) {
						continue;
					}
					while (Ptr < Tail && '>' != *Ptr) {
						if (strchr("\r\n", *Ptr)) {
							break;
						}
						Ptr++;
					}
					if (*Ptr == '>' && (0 == ::_strnicmp(Prev, "a ", 2))) {
						BOOL bIsEnd = Ptr[-1] == '/';
						char chr = *Ptr;
						*Ptr = '/0';
						if (!isSong) {
							tag_a_filter(url, Prev, isSong);
							*Ptr = chr;
						}
						else {
							*Ptr = chr;
							if (!bIsEnd && (Prev[0] == 'a' || Prev[0] == 'A')) {
								Prev = ++Ptr;
								while (Ptr < Tail && *Ptr != '<') {
									//有时会有内容存在换行的情况
									if (0 == ::_strnicmp(Ptr, "\r\n", 2)) {
										//break;
										Ptr = Ptr + 2;
										Prev = Ptr;
									}
									Ptr++;
								}
								chr = *Ptr;
								*Ptr = '\0';
								xTRACE("%s:\"%s\",\r\n", tab[i], Prev);
								g_htmlFile.Printf("%s:\"%s\",\r\n", tab[i], Prev);
								*Ptr = chr;
								i++;
								if (isSong&&i == 2) {
									xTRACE("},\r\n");
									g_htmlFile.Printf("},\r\n");
									break;
								}
							}
						}
					}
				}
			}
		}
	}
	return;
}
/**************************************************************
* _url_spider
*链接分析
*/
static void _url_spider(IN PURL_HELPER u, IN BOOL isSong) {

	DBLOB	hostName;
	DBLOB	relPath;
	char   *Ptr;
	char   *Prev;
	char   *Tail;
	int     i;
	USHORT  PortNumber;
	SOCKET fd = INVALID_SOCKET;
	if (g_bTrace) {
		xTRACE("%s\r\n", (const char*)*u);
	}
	::InterlockedIncrement(&g_iSpider);
	//
	// Try to parse this pszUrl,and prepare hostName & relUrl respectively
	//
	if (!u->hostName(hostName, PortNumber)) {
		goto _BAIL_;
	}
	u->relPath(relPath);

	fd = conn2Peer(hostName.GetPointer<const char*>(), PortNumber);
	if (fd != SOCKET_ERROR) {
		DBLOB blob(1024 * 512);
		//
		// Build an HTTP request by pszUrl,and send it to server
		//
		if (PortNumber != 80) {
			i = blob.Printf(
				"GET %s HTTP/1.1\r\n"
				"Host: %s:%d\r\n"
				"User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko\r\n"
				"Accept-Type: */*\r\n"
				"Connection: close\r\n"
				"\r\n",
				relPath.GetPointer<const char*>(),
				hostName.GetPointer<const char*>(),
				PortNumber
			);
		}
		else {
			i = blob.Printf(
				"GET %s HTTP/1.1\r\n"
				"Host: %s\r\n"
				"User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko\r\n"
				"Accept-Type: */*\r\n"
				"Connection: close\r\n"
				"\r\n",
				relPath.GetPointer<const char*>(),
				hostName.GetPointer<const char*>()
			);
		}
		//	xTRACE("%s", blob.GetPointer<char*>());
		if (i == send(fd, (const char*)blob, i, 0)) {
			//printf("connect and send %s%s success\r\n", hostName.GetPointer<const char*>(), relPath.GetPointer<const char*>());
			DBLOB header(1);
			header.GrowBuffer(8192);
			blob.Cleanup();
			do {
				if (0 >= blob.GrowBuffer(8192)) {
					//
					// we're running out of memory, and can do nothing with it!
					//
					break;
				}
				if (InterlockedCompareExchange(&g_bClosed, FALSE, FALSE)) {
					goto _BAIL_;
				}
				i = recv(fd, blob.GetPointer<char*>(blob.GetSize()), 8192, 0);
				if (i <= 0) {
					//
					// closed by our peer, and we have to bail out now
					//
					break;
				}
				blob.IncSize(i);
				if (blob.GetSize() > (1024 * 1024 * 4) && !isSong) {
					//
					// It's too large, and we'll discard it now 
					//
					goto _BAIL_;
				}
				if (header.GetSize() == 0) {
					Ptr = blob.GetPointer<char*>();
					Tail = blob.GetTailPointer<char*>();
					Prev = Ptr;
					while (Ptr < Tail) {
						if ('\n' == *Ptr++) {
							while ('\r' == *Ptr) {
								Ptr++;
							}
							if ('\n' == *Ptr) {
								break;
							}
						}
					}
					if (Ptr < Tail) {
						Ptr++;
						i = (INT)(Ptr - Prev);
						header.PutIntoBlob(Prev, i);
						header.Trim();
						blob.Pop(i);
						DBLOB tvalue;
						if (GetHeader(header, "Content-Type", tvalue)) {
							//	if (0 != _strnicmp(tvalue, "text/", 5) && 0 != _strnicmp(tvalue, "image/", 6)) {
							/******************************************************************************************************************/
							if (0 != _strnicmp(tvalue, "text/", 5)) {
								if (::InterlockedCompareExchange(&g_bOnline, FALSE, FALSE) || 0 != _strnicmp(tvalue, "image/", 6) && !isSong) {
									goto _BAIL_;
								}
							}
						}
						tvalue.Cleanup();
						if (GetHeader(header, "Content-Length", tvalue)) {
							ULONG len = ::strtoul(tvalue, NULL, 10);
							if (len < 1024 * 8 || len >(1024 * 1024 * 4) && !isSong) {
								goto _BAIL_;
							}
						}
						tvalue.Cleanup();
						GetHeader(header, "Content-Encoding", tvalue);
						if (tvalue.GetSize() && !isSong) {
							xTRACE("discard Content-Encoding:%s\r\n", tvalue.GetPointer<char*>());
							goto _BAIL_;
						}

					}
				}
			} while ("I need more ...");
			url_filter(u, header, blob, isSong);
		}
	}
	else {
		if (isSong) {
			xTRACE("songName:\"\",\r\nimg:\"\",\r\nartist:\"\",\r\nalbum:\"\"},\r\n");
			g_htmlFile.Printf("songName:\"\",\r\nimg:\"\",\r\nartist:\"\",\r\nalbum:\"\"},\r\n");
		}
		xTRACE("\r\nOops, failed to connect to [%s:%d]\r\n", hostName.GetPointer<const char*>(), PortNumber);
	}
_BAIL_:
	if (fd != INVALID_SOCKET) {
		closesocket(fd);
	}
	InterlockedDecrement(&g_iSpider);
	return;
}
//往队列里加u
void url_spider(PURL_HELPER u, IN BOOL bImportant) {
	if (!u->IsValid()) {
		delete u;
		return;
	}
	::EnterCriticalSection(&g_hSyn);//锁
	if ((bImportant || g_iUrlNumber < g_maxUrlNumber) && tableUrl.count(*u) == 0) {
		//if (tableUrl.count(*u) == 0) {
		if (g_iUrlNumber >= g_maxUrlNumber) {
			if (!g_quePending.empty()) {
				URL_HELPER *foo = g_quePending.back();
				g_quePending.pop_back();
				delete foo;
			}
		}
		g_iUrlNumber++;
		tableUrl.insert(*u);
		if (bImportant) {
			g_quePending.push_front(u);
		}
		else {
			g_quePending.push_back(u);
		}
		::ReleaseSemaphore(g_hSemaphore, 1, NULL);
	}
	else {
		//	xTRACE("url collision:[%s]\r\n", (const char*)*u);
		delete u;
	}
	::LeaveCriticalSection(&g_hSyn);
	return;
}
//从队列取url进行分析
static DWORD CALLBACK url_worker(IN LPVOID lpCtx) {
	URL_HELPER *u;
	BOOL        bClosed;
	do {
		if (WAIT_OBJECT_0 != ::WaitForSingleObject(g_hSemaphore, INFINITE)) {//指定的对象的状态没有发出信号
			break;
		}
		::EnterCriticalSection(&g_hSyn);
		if (!g_quePending.empty()) {
			u = g_quePending.front();
			g_quePending.pop_front();
		}
		else {
			u = NULL;
		}
		bClosed = g_bClosed;
		::LeaveCriticalSection(&g_hSyn);
		if (u) {
			if (!bClosed) {
				_url_spider(u, FALSE);/****/
			}
			else {
				delete u;
			}
		}
	} while (TRUE);
	return 0;
}
//输出html
static DWORD CALLBACK htmlWorker(IN LPVOID lpCtx) {
	do {
		Sleep(3000);
		if (0 == ::InterlockedCompareExchange(&g_iSpider, 0, 0)) {
			::EnterCriticalSection(&g_hSyn);
			if (g_quePending.empty()) {
				::LeaveCriticalSection(&g_hSyn);
				break;
			}
			::LeaveCriticalSection(&g_hSyn);
		}
	} while (TRUE);
	DeleteFile("MusicPlayer.html");
	g_htmlFile.Printf("];\r\n");
	g_htmlFile.Printf("</script>\r\n</body>\r\n</html>\r\n");
	HANDLE hFile = CreateFile("MusicPlayer.html", GENERIC_WRITE, 0, NULL, CREATE_ALWAYS, 0, NULL);

	DWORD i = 0;
	WriteFile(hFile, g_htmlFile.GetPointer<PVOID>(), g_htmlFile.GetSize(), &i, NULL);
	CloseHandle(hFile);
	xTRACE("\r\n\r\nThat's all\r\n\r\n");
	ShellExecute(NULL, "open", "MusicPlayer.html", NULL, NULL, SW_SHOW);
	::SetEvent(g_hSignal);
	xTRACE("\r\n歌曲总数%d\r\n", m_number);
	return 0;
}
static void htmlSWrite() {
	g_htmlFile.Cleanup();
	g_htmlFile.Printf("<!DOCTYPE html>\r\n"
		"<html>\r\n"
		"<head>\r\n"
		"<meta charset=\"UTF-8\">\r\n"
		"<meta name=\"viewport\" content=\"width = device-width, initial-scale = 1, maximum-scale = 1, user-scalable = no\"/>\r\n"
		"<title>Music Player of Spider</title>\r\n"
		"<link rel=\"stylesheet\" href=\"css/player.css\" />\r\n"
		"<script type = \"text/javascript\" src = \"js/jquery.min.js\"></script>"
		"<script src=\"js/player.js\"></script>\r\n"
		"</head>\r\n"
		"<body>\r\n"
		"<div class=\"wrapper\"> \r\n"
		"<div id=\"bg\" class=\"bg\">\r\n<div class = \"bg-cover\"></div>\r\n</div>"
		"<div class = \"title\">\r\n"
		"<div class = \"music-info\">\r\n"
		"<div class = \"song\" id = \"songName\"></div>\r\n<div class = \"artist\" id = \"artist\"></div>\r\n"
		"</div><div class = \"query\">\r\n"
		"<input type = \"text\" id = \"qureySong\" maxlength = \"100\" />\r\n"
		"<div class = \"search\"></div>\r\n"
		"<label id = \"result\"></label>\r\n"
		"</div></div>\r\n"
		"<div class=\"play-board\">\r\n"
		"<img id = \"needle\" class = \"play-needle pause-needle\" src = \"resource/images/play_needle.png\" / >\r\n"
		"<div class = \"disk-bg\"></div>\r\n"
		"<div class = \"disk-cover disk-cover-animation\" style = \"display: none\">\r\n"
		"<img class = \"album\" src = \"resource/images/placeholder_disk_play_song.png\" / >\r\n"
		"<img class = \"disk-border\" src = \"resource/images/play_disc.png\" / >\r\n"
		"</div>\r\n"
		"<div class = \"disk-cover disk-cover-animation\">\r\n"
		"<img class = \"album\" src = \"resource/images/placeholder_disk_play_song.png\" / >\r\n"
		"<img class = \"disk-border\" src = \"resource/images/play_disc.png\" / >\r\n"
		"</div>\r\n"
		"<div class = \"disk-cover disk-cover-animation\" style = \"display: none\">\r\n"
		"<img class = \"album\" src = \"resource/images/placeholder_disk_play_song.png\" / >\r\n"
		"<img class = \"disk-border\" src = \"resource/images/play_disc.png\" / >\r\n</div>\r\n"
		"<audio id = \"player\"></audio>\r\n"
		"<div class = \"footer\">\r\n"
		"<div class = \"process\" id = \"process\">\r\n"
		"<span id = \"currentTime\">00:00</span>\r\n"
		"<div class = \"process-bar\">\r\n"
		"<div class = \"rdy\"></div>\r\n"
		"<div class = \"cur\">\r\n"
		"<span id = \"processBtn\" class = \"process-btn c-btn\"></span>\r\n"
		"</div>\r\n</div>\r\n"
		"<span id = \"totalTime\">00:00</span>\r\n</div>\r\n"
		"<div class = \"control\" id = \"controls\">\r\n"
		"<span class = \"c-btn loop-btn\"></span>\r\n"
		"<span class = \"pre c-btn\"></span>\r\n"
		"<span class = \"play c-btn\"></span>\r\n"
		"<span class = \"pause c-btn\" style = \"display: none\"></span>\r\n"
		"<span class = \"next c-btn\"></span>\r\n"
		"<span class = \"c-btn list-btn\"></span>\r\n"
		"</div>\r\n</div>\r\n</div>\r\n"
		"<div class = \"play-list\" id = \"playList\">\r\n"
		"<div class = \"list-title\">PlayList(<span id = \"playListCount\">0</span>)</div>\r\n"
		"<ul class = \"list-content\" id = \"listContent\">\r\n"
		"</ul>\r\n</div>\r\n</div>\r\n"
		"<script type=\"text/javascript\">\r\n"
		"var playlist = [\r\n"
	);
	return;
}
/**************************************************************
* main
*     the entry point for this program
*/
int _cdecl main(int argc, char *argv[]) {

	WSADATA  wsaData;
	INT      i;
	//
	// All in all, we must call this function to load ws2_32.dll module into this process space.
	// Actually, all socket's functions are implemented in this module.
	//
	WSAStartup(0x0202, &wsaData);

	::InitializeCriticalSection(&g_hSyn);
	::InitializeCriticalSection(&g_hSyn);

	g_hSemaphore = ::CreateSemaphore(NULL, 0, MAXLONG, NULL);
	g_hSignal = ::CreateEvent(NULL, TRUE, TRUE, NULL);

	for (i = 0; i < MAX_SPIDER_NUMBER; i++) {
		HANDLE h = ::CreateThread(NULL, 0, url_worker, NULL, 0, NULL);
		CloseHandle(h);
	}
	char Url[MAX_URL_LENGTH + 1];
	//
	// http://699pic.com/ or http://image.baidu.com/ or http://www.photophoto.cn/
	// http://blog.sina.com.cn/lm/travel/
	//
	do {
		//printf("\r\n\r\nPlease input url or cmd: ");
		xTRACE("\r\n\r\nPlease input url or cmd: ");
		i = 0;
		ReadFile(GetStdHandle(STD_INPUT_HANDLE), Url, MAX_URL_LENGTH, (DWORD*)&i, NULL);
		while (--i >= 0) {
			if (NULL == strchr(" \r\n\t", Url[i])) {
				break;
			}
		}
		Url[++i] = '\0';
		if (i > 0) {
			if (0 == ::_stricmp(Url, "exit") || 0 == ::_stricmp(Url, "quit")) {
				xTRACE("\r\n\r\nThat's so much, and bye ...\r\n\r\n");
				break;
			}
			else if (0 == ::_stricmp(Url, "iSpider")) {
				xTRACE("\r\ng_iSpider:%d\r\n", g_iSpider);
			}
			else if (0 == _stricmp(Url, "traceon")) {
				g_bTrace = true;
			}
			else if (0 == _stricmp(Url, "traceoff")) {
				g_bTrace = false;
			}
			else if (0 == _stricmp(Url, "online")) {
				InterlockedExchange(&g_bOnline, TRUE);
				xTRACE("\r\nEnable online mode...\r\n");
			}
			else if (0 == _stricmp(Url, "offline")) {
				InterlockedExchange(&g_bOnline, FALSE);
				xTRACE("\r\nEnable offline mode...\r\n");
			}
			else if (0 == _strnicmp(Url, "maxNumber", 9)) {
				ULONG n = strtoul(&Url[10], NULL, 10);
				g_maxUrlNumber = (LONG)n;
				xTRACE("\r\nmaxNumber:%d\r\n", n);
			}
			else if (0 == _stricmp(Url, "urlNumber")) {
				::EnterCriticalSection(&g_hSyn);
				LONG pending = (LONG)g_quePending.size();
				::LeaveCriticalSection(&g_hSyn);
				xTRACE("\r\ng_iUrlNumber:%d g_maxUrlNumber:%d Pending:%d\r\n", g_iUrlNumber, g_maxUrlNumber, pending);
			}
			else if (0 == _stricmp(Url, "stop")) {
				g_iUrlNumber = g_maxUrlNumber + 10;
				InterlockedExchange(&g_bClosed, TRUE);
				xTRACE("\r\nIt's being closed ...\r\n");
			}
			else if (strchr(Url, '.') != NULL) {
				if (0 != _strnicmp(Url, "http://", 7)) {
					char Tmp[8192];
					strcpy(Tmp, "http://");
					strcat(Tmp, Url);
					strcpy(Url, Tmp);
				}
				URL_HELPER *u = new URL_HELPER(Url);
				if (WAIT_OBJECT_0 != ::WaitForSingleObject(g_hSignal, 1000)) {
					//	xTRACE("\r\nSomething is pending, please try later\r\n");
					url_spider(u, TRUE);
					continue;
				}
				ResetEvent(g_hSignal);
				htmlSWrite();
				g_iUrlNumber = 0;
				g_bClosed = FALSE;
				g_fi = 0;
				tableUrl.clear();
				tableSong.clear();
				url_spider(u, FALSE);
				CreateThread(NULL, 0, htmlWorker, NULL, 0, NULL);
			}
			else {
				xPANIC("[%s] seems not be a valid url\r\n", Url);
			}
		}
	} while ("Go ahead ...");
	//
	// We have done, and just dump (unload) this module out of this process space
	//
	WSACleanup();

	return 0;
}