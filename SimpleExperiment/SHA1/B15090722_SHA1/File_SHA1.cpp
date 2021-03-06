// File_SHA1.cpp: 实现文件
//tab子框实现

#include "stdafx.h"
#include "B15090722_SHA1.h"
#include "File_SHA1.h"
#include "afxdialogex.h"
#include "SHA1.h"
#define Max 8190

// File_SHA1 对话框

IMPLEMENT_DYNAMIC(File_SHA1, CDialogEx)

File_SHA1::File_SHA1(CWnd* pParent /*=nullptr*/)
	: CDialogEx(IDD_DIALOG2, pParent)
{

}

File_SHA1::~File_SHA1()
{
}

void File_SHA1::DoDataExchange(CDataExchange* pDX)
{
	CDialogEx::DoDataExchange(pDX);
	DDX_Text(pDX, IDC_EDIT1, m_strFilePath);
	DDX_Text(pDX, IDC_STATIC_SHA1, m_static);
}


BEGIN_MESSAGE_MAP(File_SHA1, CDialogEx)
	ON_WM_DROPFILES()
	ON_BN_CLICKED(IDC_BNFILE, &File_SHA1::OnBnClickedBnfile)
	ON_BN_CLICKED(IDC_BNSHA, &File_SHA1::OnBnClickedBnsha)
END_MESSAGE_MAP()


// File_SHA1 消息处理程序


/*************************************************************
函数名称:	OnBnClickedBnfile
函数说明:	打开文件按钮
************************************************************/
void File_SHA1::OnBnClickedBnfile()
{
	UpdateData(true);
	CFileDialog dlg(TRUE, NULL, NULL,
		OFN_HIDEREADONLY | OFN_OVERWRITEPROMPT,
		(LPCTSTR)_TEXT("All Files (*.*)|*.*||"), NULL);
	if (dlg.DoModal() == IDOK)
		m_strFilePath = dlg.GetPathName();
	else
		return;
	UpdateData(FALSE);
}

/*************************************************************
函数名称:	OnBnClickedBnsha
函数说明:	SHA1按钮
************************************************************/
void File_SHA1::OnBnClickedBnsha()
{
	GetDlgItemText(IDC_EDIT1, m_strFilePath);
	if (!m_strFilePath.IsEmpty()) {
		int err = 0;
		USES_CONVERSION;									/*声明标识符（CString -> char*）*/
		char * file_info = T2A(m_strFilePath);

		FILE *fp = NULL;									/*文件操作		*/
		fopen_s(&fp, file_info, "r");
		if (!fp) {
			m_static.Format(_T("文件地址错误：%s "), file_info);
			GetDlgItem(IDC_STATIC_SHA1)->SetWindowText(m_static);
			return;
		}

		SHA1 sha;											/*SHA1 HASH操作	*/
		sha.Reset();
		char* message_array = new char[8192];
		while (fgets(message_array, Max, fp) != NULL) {
			err = sha.Input(
				(const unsigned char *)message_array,
				strlen(message_array)
			);
			if (err) {
				m_static = _T("文件太大，超过 2^64 bit ( 2097152 TB )");
				GetDlgItem(IDC_STATIC_SHA1)->SetWindowText(m_static);
				return;
			}
		}
		delete(message_array);
		sha.Result(&m_static);

		MessageBox(_T("消息摘要已生成！"), _T("提示"), MB_OK);
		GetDlgItem(IDC_STATIC_SHA1)->SetWindowText(m_static);

		int nStatus = ((CButton*)GetDlgItem(IDC_CHECK1))->GetCheck();
		if (nStatus) {										/*输出到文件		*/
			char* filepath = new char[m_strFilePath.GetLength() + 20];
			int count = m_strFilePath.GetLength();
			strcpy_s(filepath, count + 20, file_info);
			while (*(filepath + count) != '\\') {
				count--;
			}
			//filepath为选择的文件的路径
			*(filepath + ++count) = '\0';

			char *des = "文件SHA1摘要.txt";
			while (*des != '\0') {
				*(filepath + count++) = *des++;
			}
			*(filepath + count) = '\0';
			//filepath为 选择的文件的路径\文件SHA1摘要.txt

			char * message_dig = T2A(m_static);
			FILE *fp = NULL;
			fopen_s(&fp, filepath, "w+");
			fprintf(fp, "%s", message_dig);
			fclose(fp);
		}
	}
	else {
		MessageBox(_T("请选择要生成消息摘要的文件\n"), _T("提示"), MB_OK);
		return;
	}
}

/*************************************************************
函数名称:	OnDropFiles
函数说明:	文件拖拽
************************************************************/
void File_SHA1::OnDropFiles(HDROP hDropInfo)
{
	WCHAR wcStr[MAX_PATH];
	DragQueryFile(hDropInfo, 0, wcStr, MAX_PATH);			/*获得拖曳文件的文件名  */
	GetDlgItem(IDC_EDIT1)->SetWindowText(wcStr);
	DragFinish(hDropInfo);									/*拖放结束后,释放内存*/
	CDialogEx::OnDropFiles(hDropInfo);
}
