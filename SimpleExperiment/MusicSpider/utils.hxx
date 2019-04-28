/**************************************************************
 *
 * Module:
 *
 *        Utils.hxx
 *
 * Author:
 *
 *        WANG HAIPING (i_free001@njupt.edu.cn)
 *
 * History:
 *
 *        Apr.10, 2017 Nanjing (created)
 *
 * Notes: 
 *
 */
#ifndef _HAIPING_19701224_UTILS_HXX_
#define _HAIPING_19701224_UTILS_HXX_
 //
 // macros for convenience
 //
#define MIN_HOST_NAME_LENGTH    3 // a.b
#define MAX_HOST_NAME_LENGTH    (256 - 1)
#define MAX_URL_LENGTH          (8192  - 1)
#define MAX_HTTP_HEADER_LENGTH  (1024*32 - 1)

#define PTR_DIFF(_F_,_B_)      ( (PBYTE)(_F_) - (PBYTE)(_B_))

#define xTRACE                  TRACE_HELPER(FALSE,__FILE__,__LINE__)
#define xPANIC                  TRACE_HELPER(TRUE,__FILE__,__LINE__)
#define ASSERT                  assert

class TRACE_HELPER {
private:
	const char *const	m_pszFileName;
	const int			m_nLineNo;
	BOOL                m_bPanic;
public:
	TRACE_HELPER(BOOL bPanic, const char *pszFileName, int nLineNo)
		:m_pszFileName(pszFileName)
		, m_nLineNo(nLineNo)
		, m_bPanic(bPanic)
	{
	}
#pragma warning(push)
#pragma warning(disable : 4793)
	void __cdecl operator()(const char *pszFmt, ...)
	{
		va_list Ptr; va_start(Ptr, pszFmt);
		Printf((const char*)pszFmt, Ptr);
		va_end(Ptr);
		return;
	}
#pragma warning(pop)
	virtual VOID Printf(const char *pszFmt, va_list val) throw() {
		if (m_bPanic) {
			printf("******File:%s Line:%d******\r\n", m_pszFileName, m_nLineNo);
		}
		vprintf(pszFmt, val);
	}
};

class DBLOB {
#ifdef _DEBUG
	#define DBG_TRACE_SIGNATURE                 0x19701224 // The day when I got my life !
	struct _DBG_TRACE_INFO {
		ULONG      Signature;
		ULONG      Length;
	};
#endif//_DEBUG
private:
	PBYTE        _Buffer;
	INT          _iSize;
	INT          _iCapacity;
public:
	INT          _iGrowDelta;
public:
	explicit DBLOB(INT iGrowDelta = 8192) throw()
	:_Buffer(NULL)
	, _iSize(0)
	, _iCapacity(0)
    ,_iGrowDelta(iGrowDelta)
	{
		ASSERT(iGrowDelta >= 0);
	}
	explicit DBLOB(DBLOB &Other) throw()
	:_Buffer(NULL)
	, _iSize(0)
	, _iCapacity(0)
	, _iGrowDelta(Other._iGrowDelta)
	{
		if (Other._iSize) {
			PutIntoBlob(Other._Buffer, Other._iSize);
		}
	}
	virtual ~DBLOB(IN VOID) throw() {
		Cleanup();
	}
	inline const INT GetSize(IN VOID) const throw() {
		return _iSize;
	}
	template<class _P_>
	inline _P_ GetPointer(IN INT Off = 0) throw() {
		if (_Buffer && _Buffer[_iSize] != '\0') {
			_Buffer[_iSize] = '\0';
		}
		return (_P_)&_Buffer[Off];
	}
	template<class _P_>
	inline _P_ GetTailPointer(IN VOID) throw() {
		return GetPointer<_P_>(GetSize());
	}
	inline operator const char*() throw() {
		return GetPointer<const char*>();
	}
	inline BOOL operator ==(const DBLOB &Other) {
		ASSERT(this != &Other);
		BOOL fYES = FALSE;
		if (_iSize == Other._iSize && 0 == ::memcmp(_Buffer, Other._Buffer, _iSize)) {
			fYES = TRUE;
		}
		return fYES;
	}
	inline BOOL operator ==(const char *szToken) {
		ASSERT(szToken != NULL);
		INT i = (int)::strlen(szToken);
		BOOL fYES = FALSE;
		if (_iSize == i && 0 == ::memcmp(_Buffer, szToken, _iSize)) {
			fYES = TRUE;
		}
		return fYES;
	}
	inline int operator =(const DBLOB &Other) {
		ASSERT(this != &Other);
		Cleanup();
		return *this += Other;
	}
	inline int operator +=(const DBLOB &Other) {
		ASSERT(this != &Other);
		if (Other._iSize > 0) {
			PutIntoBlob(Other._Buffer, Other._iSize);
		}
		return _iSize;
	}
	inline int  operator =(const char* szToken) {		
		Cleanup();
		return *this += szToken;
	}
	inline int operator =(char *szToken) {
		Cleanup();
		return *this +=szToken;
	}
	inline int  operator +=(const char* szToken) {
		ASSERT(szToken != NULL);
		int i = (int)::strlen(szToken);
		return PutIntoBlob((PVOID)szToken, i);
	}
	inline int operator +=(char *szToken) {
		ASSERT(szToken != NULL);
		int i = (int)::strlen(szToken);
		return PutIntoBlob(szToken, i);
	}
	inline int IncSize(INT iSize) {
		ASSERT(iSize >= 0);
		ASSERT((iSize + _iSize) <= _iCapacity);
		_iSize += iSize;
		if (_Buffer) {
			_Buffer[_iSize] = '\0';
		}
		return _iSize;
	}
	inline int GrowBuffer(INT iGrowSize) {
		ASSERT(iGrowSize >= 0);
		int iTotalSize = _iSize + iGrowSize;
		ASSERT(iTotalSize >= iGrowSize);
		if (iTotalSize > _iCapacity) {
			ASSERT(_iGrowDelta >= 0);
			iTotalSize += _iGrowDelta;
#ifdef _DEBUG
			PBYTE Ptr = new BYTE[sizeof(_DBG_TRACE_INFO) + iTotalSize + 1 + sizeof(_DBG_TRACE_INFO)];
			if (!Ptr) {
				return -1;
			}
			_DBG_TRACE_INFO *d = (_DBG_TRACE_INFO*)&Ptr[0];
			d->Signature = DBG_TRACE_SIGNATURE;
			d->Length    = iTotalSize + 1;
			d = (_DBG_TRACE_INFO*)&Ptr[sizeof(_DBG_TRACE_INFO) + iTotalSize + 1];
			d->Signature = DBG_TRACE_SIGNATURE;
			d->Length    = iTotalSize + 1;
			Ptr         += sizeof(_DBG_TRACE_INFO);
			CheckBuffer();
#else //_DEBUG
			PBYTE Ptr = new BYTE[iTotalSize + 1];
			if (!Ptr) {
				return -1;
			}
#endif//_DEBUG
			if (_iSize) {
				ASSERT(_Buffer != NULL);
				::memcpy(Ptr, _Buffer, _iSize);
			}
#ifdef _DEBUG
			if (_Buffer) {
				_Buffer -= sizeof(_DBG_TRACE_INFO);
			}
			delete[]_Buffer;
#endif//_DEBUG
			_Buffer    = Ptr;
			_iCapacity = iTotalSize;
		}
		return _iCapacity;
	}
	inline int PutIntoBlob(PVOID lpToken, INT cbToken) {
		ASSERT(lpToken != NULL);
		if (cbToken < 0) {
			//
			// If cbToken is negative, we assume lpToken is a pointer to an ANSI string.
			//
			cbToken = (INT)::strlen((char*)lpToken);
		}
		if (cbToken == 0) {
			//
			// It's not necessary,but legal!
			//
			return _iSize;
		}
		if (cbToken != 0) {
#ifdef _DEBUG
			if (_Buffer) {
				//
				// never overlap with our internal _Buffer, please
				//
				ASSERT(lpToken >= (PVOID)&_Buffer[_iCapacity + 1] || &((PBYTE)lpToken)[cbToken] < &_Buffer[0]);
			}
#endif//_DEBUG			
			if (0 >= GrowBuffer(cbToken)) {
				return 0;
			}
			memcpy(&_Buffer[_iSize], lpToken, cbToken);
			_iSize         += cbToken;
			_Buffer[_iSize] = '\0';
		}
		//
		// return the total length of data put in _Buffer
		//
		return _iSize;
	}
	inline int Trim(IN VOID) {
		if (_iSize) {
			PBYTE Ptr  = _Buffer;
			PBYTE Tail = &Ptr[_iSize];
			while (Ptr < Tail && ::strchr(" \t\r\n", *Ptr)) {
				Ptr++;
			}
			while (--Tail >= Ptr && ::strchr(" \t\r\n", *Tail)) {}
			++Tail;
			int i = PTR_DIFF(Tail, Ptr);
			if (i != _iSize) {
				if (i && Ptr > _Buffer) {
					::memmove(&_Buffer[0], &Ptr[0], i);
				}
				_iSize          = i;
				_Buffer[_iSize] = '\0';
			}
		}
		return _iSize;
	}
	inline int Pop(INT sizeToPop){
		ASSERT(sizeToPop >= 0 && sizeToPop <= _iSize);
		if (sizeToPop) {
			::memmove(&_Buffer[0], &_Buffer[sizeToPop], _iSize - sizeToPop + 1);
			_iSize -= sizeToPop;
		}
		return _iSize;
	}
	/**************************************************************
	* xchunk_printf_v
	*
	*/
	inline int Printf_v(IN const char* szFmt, va_list marker) {

		char    szFoo[512];
		char    *Ptr = &szFoo[0];
		INT      size = 512;
		INT      i;
		ASSERT(NULL != szFmt);

		do {
			__try {
#pragma warning(disable:4996)
				i = _vsnprintf(Ptr, size, szFmt, marker);
#pragma warning(default:4996)
			}
			__except (EXCEPTION_EXECUTE_HANDLER) {
				//
				// exception thrown, and break out with failure
				//
				break;
			}
			if (i > 0 && i < size) {
				PutIntoBlob(Ptr, i);
				if (&szFoo[0] != Ptr) {
					delete []Ptr;
				}
				return i;
			}
			if (&szFoo[0] != Ptr) {
				delete []Ptr;
			}
			size <<= 1;
			Ptr = (char*)new char[size];
		} while (NULL != Ptr && size <= (1024 * 64));
		if (Ptr && &szFoo[0] != Ptr) {
			delete []Ptr;
		}
		return 0;
	}
	inline int Printf(const char* szFmt, ...) {
		INT       i;
		va_list   marker;
		va_start(marker, szFmt);
		i = Printf_v(szFmt, marker);
		va_end(marker);
		return i;
	}
	inline void Cleanup() throw() {
		if (_Buffer) {
#ifdef _DEBUG
			CheckBuffer();
_Buffer -= sizeof(_DBG_TRACE_INFO);
#endif//_DEBUG
delete[]_Buffer;
_Buffer = NULL;
		}
		_iSize = _iCapacity = 0;
		return;
	}
	inline const char* toUpperCase(IN VOID) {
		for (int i = 0; i < _iSize; i++) {
			BYTE chr = _Buffer[i];
			if (chr >= 'a' && chr <= 'z') {
				_Buffer[i] -= ('a' - 'A');
			}
		}
		return GetPointer<const char*>(0);
	}
	inline const char* toLowerCase(IN VOID) {
		for (int i = 0; i < _iSize; i++) {
			BYTE chr = _Buffer[i];
			if (chr >= 'A' && chr <= 'Z') {
				_Buffer[i] += ('a' - 'A');
			}
		}
		return GetPointer<const char*>(0);
	}
	inline DWORD LoadFromFile(LPCTSTR pszFile) {
		Cleanup();
		HANDLE hFile = ::CreateFile(pszFile, GENERIC_READ, FILE_SHARE_READ, NULL, OPEN_EXISTING, FILE_FLAG_SEQUENTIAL_SCAN, NULL);
		if (hFile != INVALID_HANDLE_VALUE) {
			DWORD   fSize = ::GetFileSize(hFile, NULL);
			if (fSize > 0 && fSize != INVALID_FILE_SIZE) {
				DWORD  Ttl = 0;
				DWORD  i;
				while (Ttl < fSize) {
					i = fSize - Ttl;
					if (i > (1024 * 1024 * 4)) {
						i = 1024 * 1024 * 4;
					}
					if (0 >= GrowBuffer((int)i)) {
						break;
					}
					if (!::ReadFile(hFile, &_Buffer[_iSize], i, &i, NULL) || i == 0) {
						break;
					}
					IncSize((int)i);
					Ttl += i;
				}
			}
			::CloseHandle(hFile);
		}
		return _iSize;
	}
	inline DWORD FlushIntoFile(LPCTSTR pszFile) {
		HANDLE   hFile = ::CreateFile(pszFile, GENERIC_WRITE, 0, NULL, CREATE_NEW, FILE_FLAG_SEQUENTIAL_SCAN, NULL);
		DWORD    i;
		if (!::WriteFile(hFile, _Buffer, _iSize, &i, NULL)) {
			i = 0;
		}
		::CloseHandle(hFile);
		return i;
	}
protected:
#ifdef _DEBUG
	inline void CheckBuffer(IN VOID) {
		if (_Buffer) {
			_DBG_TRACE_INFO *d = (_DBG_TRACE_INFO*)(_Buffer - sizeof(_DBG_TRACE_INFO));
			ASSERT(d->Signature == DBG_TRACE_SIGNATURE);
			_DBG_TRACE_INFO *b = (_DBG_TRACE_INFO*)&_Buffer[d->Length];
			ASSERT(b->Signature == DBG_TRACE_SIGNATURE);
			ASSERT(b->Length == d->Length);
		}
		//
		// Keep the compiler happy ...
		//
		return;
	}
#endif//_DEBUG
};
//
// a helper for in-place pointer
//
class INP;
typedef class INP *PINP;
class INP {
private:
	PBYTE       _Pointer;           // The pointer to whatever you like
	USHORT      _Length;            // the length of what the _Pointer is pointed to 
	BYTE        _chrTerminal;       // The terminal char
	BYTE        _plsFree;           // free this pointer if TRUE
public:
	INP(IN VOID)
	:_Pointer(NULL)
	, _Length(0)
	, _chrTerminal(0)
	, _plsFree(0)
	{}
	~INP(IN VOID) { Cleanup();}
	INP(INP &Other)
	:_Pointer(Other._Pointer)
	,_Length(Other._Length)
	,_chrTerminal(0)
	,_plsFree(0)
	{}
	template<typename _P_>
	inline _P_ GetPointer(IN VOID) {
		return (_P_)_Pointer;
	}
	inline INT GetLength(IN VOID) {
		return (INT)_Length;
	}
	inline BOOL operator == (IN const char *szToken) {
		int i = (int)::strlen(szToken);
		return i == _Length && 0 == ::memcmp((char*)_Pointer, szToken,i);
	}
	inline BOOL operator != (IN const char *szToken) {
		int i = (int)::strlen(szToken);
		return i != _Length ||  0 != ::memcmp((char*)_Pointer, szToken, i);
	}
	inline BOOL operator == (INP  &Other) {
		return &Other == this || (Other._Pointer == _Pointer && Other._Length == _Length);
	}
	inline BOOL operator != (INP &other) {
		return other._Pointer != _Pointer || other._Length != _Length;
	}
	inline PVOID SetPointer(PVOID Pointer, IN INT Length,IN BOOL bNewly = FALSE) {		
		ASSERT(Length >= 0); // must not be native 
		ASSERT(Length == 0 || Pointer != NULL);		 
		Cleanup();
		if (!bNewly) {
			_Pointer = (PBYTE)_Pointer;
			_Length  = Length;
		}
		else {
			if (Length > 0) {				
				_Pointer = new BYTE[Length + 1];
				if (_Pointer) {
					_plsFree = true;
					::memcpy(_Pointer, Pointer, Length);
					_Pointer[Length] = '\0';
					_Length = Length;
				}
			}
		}
		return	_Pointer;
	}
	inline int Printf(IN const char *szFmt, ...) {
		va_list   marker;
		DBLOB     blob;
		va_start(marker, szFmt);
		INT i = Printf(szFmt, marker);
		va_end(marker);
		return i;
	}
	inline int Printf(IN const char *szFmt,va_list marker) {		
		DBLOB     blob;
		blob.Printf_v(szFmt, marker);
		if (SetPointer(blob.GetPointer<PVOID>(), blob.GetSize(), true)) {
			return _Length;
		}
		return 0;
	}
	inline BYTE TruncPointer(IN VOID) {
		ASSERT(_chrTerminal == 0);
		ASSERT(_Pointer     != NULL);
		_chrTerminal        = _Pointer[_Length];
		_Pointer[_Length]   = '\0';
		return _chrTerminal;
	}
	inline BYTE UnTruncPointer(IN VOID) {
		ASSERT(_Pointer != NULL);
		ASSERT(_Pointer[_Length] == '\0');
		BYTE chr          = _chrTerminal;		
		_Pointer[_Length] = chr;		
		_chrTerminal      = 0;
		return  chr;
	}
	inline VOID Cleanup(IN VOID) {
		if (_Pointer && _chrTerminal) {
			ASSERT(_Pointer[_Length] == '\0');
			_Pointer[_Length] = _chrTerminal;
		}
		if (_plsFree) {
			delete[]_Pointer;
		}
		_Pointer     = NULL;
		_plsFree     = FALSE;
		_Length      = 0;
		_chrTerminal = 0;
	}
};

typedef class URL_HELPER *PURL_HELPER;

class URL_HELPER {
protected:
	char   *m_pszUrl;
public:		
	URL_HELPER(IN const char *pszUrl) throw()
	:m_pszUrl(NULL)
	{		
		_uHandler(pszUrl);
	}
	URL_HELPER(const URL_HELPER &other) throw()
	:m_pszUrl(NULL)
	{
		ASSERT(other.IsValid());
		if (other.IsValid()) {
			_uHandler(other.m_pszUrl);
		}
	}
	~URL_HELPER(IN VOID) throw() {
		delete[]m_pszUrl;
	}
	inline BOOL IsValid(IN VOID) const throw() {
		return NULL != m_pszUrl;
	}
	inline URL_HELPER& operator = (const char* pszUrl) throw() {
		_uHandler(pszUrl);
		return *this;
	}
	inline URL_HELPER& operator = (char* pszUrl) throw() {
		_uHandler((const char*)pszUrl);
		return *this;
	}
	inline URL_HELPER& operator = (URL_HELPER &other) throw() {
		ASSERT(other.IsValid());
		_uHandler(other.m_pszUrl);
		return *this;
	}
	inline bool operator == (const URL_HELPER &other) {
		return 0 == ::_stricmp(other.m_pszUrl, m_pszUrl);
	}
	inline bool operator != (const URL_HELPER &other) {
		return 0 != ::_stricmp(other.m_pszUrl, m_pszUrl);
	}
	inline bool operator < (const URL_HELPER &other) const {
		return 0 > ::_stricmp(other.m_pszUrl, m_pszUrl);
	}
	inline bool operator > (const URL_HELPER &other) {
		return 0 < ::_stricmp(other.m_pszUrl, m_pszUrl);
	}
	inline bool operator == (const char *pszUrl) {
		return 0 == ::_stricmp(pszUrl, m_pszUrl);
	}
	inline bool operator != (const char *pszUrl) {
		return 0 != ::_stricmp(pszUrl, m_pszUrl);
	}
	inline operator char*() throw() {
		return m_pszUrl;
	}
	inline operator const char*() throw(){
		return (const char*)m_pszUrl;
	}
	inline BOOL hostName(DBLOB &hname, IN USHORT &portNumber) {
		portNumber = 80;
		if (IsValid()) {
			char *Ptr = &m_pszUrl[7]; if (Ptr[0] == '/') { Ptr++; portNumber = 443; }
			char *Prev = Ptr;
			while (*Ptr && strchr("/:", *Ptr) == NULL) {
				Ptr++;
			}
			INT i = (INT)PTR_DIFF(Ptr, Prev);
			if (i >= MIN_HOST_NAME_LENGTH && i < MAX_HOST_NAME_LENGTH) {
				hname.PutIntoBlob(Prev, i);
				if (':' == *Ptr) {
					ULONG n = ::strtoul(++Ptr, NULL, 10);
					if (n && n < 65535) {
						portNumber = (USHORT)n;
					}
					else {
						xPANIC("invalid url:[%s]\r\n", m_pszUrl);
					}
				}
				return TRUE;
			}
		}
		return FALSE;
	}
	inline BOOL urlPath(DBLOB &uPath) throw() {
		char *Ptr = m_pszUrl;
		int  i = 0;
		while (Ptr && *Ptr) { Ptr++; i++;}
		if (i) {
			while (--Ptr > m_pszUrl && '/' != Ptr[0]) {}
			if (Ptr > m_pszUrl) {
				i = (INT)PTR_DIFF(Ptr, m_pszUrl) + 1;
				uPath.PutIntoBlob(m_pszUrl, i);
			}
			else {
				uPath += m_pszUrl; // http://www.abc.com 
				uPath += "/";
			}
			return TRUE;
		}
		return FALSE;
	}
	inline BOOL relPath(DBLOB &uPath) throw() {
		char *Ptr = m_pszUrl;
		int  i    = 0;
		while (Ptr && *Ptr) {
			if (*Ptr == '/') {
				if (++i == 3) {
					break;
				}
			}
			Ptr++;
		}
		if (i >= 2) {
			if (i == 3) {
				uPath += Ptr;
			} else {
				uPath += "/";
			}
			return TRUE;
		}
		return FALSE;
	}
	inline BOOL urlFile(DBLOB &uFile) {
		if (IsValid()) {
			BYTE *Ptr  = (BYTE*)m_pszUrl;
			BYTE *Prev = Ptr;
			while (*Ptr) { Ptr++; }
			while(--Ptr > Prev && '/' != *Ptr){}
			if ('/' == *Ptr) {
				Prev = ++Ptr;
				while (*Ptr && strchr("?#&=", *Ptr) == NULL) {
					Ptr++;
				}
				INT i = (INT)PTR_DIFF(Ptr, Prev);
				if (i > 0) {
					uFile.PutIntoBlob(Prev, i);
					return TRUE;
				}
			}
		}
		return FALSE;
	}
	inline BOOL urlFileExt(DBLOB &uFileExt) {
		DBLOB foo;
		if (urlFile(foo)) {
			const char *i = strchr(foo.GetPointer<const char*>(), '.');
			if (i != NULL && i[1] != '\0') {
				uFileExt += i;
				return TRUE;
			}
		}
		return FALSE;
	}
	inline BOOL checkDomain(URL_HELPER &other) {
		DBLOB  ahname;
		DBLOB  bhname;
		USHORT afoo, bfoo;
		if (hostName(ahname, afoo) && other.hostName(bhname, bfoo)) {
			char *a = ahname.GetPointer<char*>();
			char *b = bhname.GetPointer<char*>();
			while (*a && *a != '.') {
				a++;
			}
			while (*b && *b != '.') {
				b++;
			}
			return 0 == ::_stricmp(a, b);
		}
		return FALSE;
	}
	inline BOOL checkDomain(const char* pszUrl) {
		URL_HELPER other(pszUrl);
		if (other.IsValid()) {
			return checkDomain(other);
		}
		return false;
	}
protected:
	inline BOOL _uHandler(const char *pszUrl) {
		if (pszUrl && (0 == ::_strnicmp(pszUrl, "http://", 7) || 0 == ::_strnicmp(pszUrl, "https://", 8))) {
			delete m_pszUrl;
			m_pszUrl = new char[::strlen(pszUrl) + 1];
			::strcpy(m_pszUrl, pszUrl);
			DBLOB  h;
			USHORT n;
			if (hostName(h, n)) {
				BYTE *Ptr = h.GetPointer<BYTE*>();
				n = 0;
				while (*Ptr) {
					if (*Ptr == '.') {
						n++;
					}
					else if (!::isalnum(*Ptr) && *Ptr != '-' && *Ptr != '_') {
						n = 0;
						xPANIC("Invalid url:[%s]\r\n", m_pszUrl);
						break;
					}
					Ptr++;
				}
				if (n >= 1) {
					return TRUE;
				}
			}
			delete[]m_pszUrl; m_pszUrl = NULL;
		}
		return FALSE;
	}
};
#endif//_HAIPING_19701224_UTILS_HXX_

