// Message_SHA1.cpp: 实现文件
//tab子框实现

#include "stdafx.h"
#include "B15090722_SHA1.h"
#include "Message_SHA1.h"
#include "afxdialogex.h"
#include "SHA1.h"

// Message_SHA1 对话框

IMPLEMENT_DYNAMIC(Message_SHA1, CDialogEx)

Message_SHA1::Message_SHA1(CWnd* pParent /*=nullptr*/)
	: CDialogEx(IDD_DIALOG1, pParent)
{

}

Message_SHA1::~Message_SHA1()
{
}

void Message_SHA1::DoDataExchange(CDataExchange* pDX)
{
	CDialogEx::DoDataExchange(pDX);
	DDX_Text(pDX, IDC_EDIT1, m_strMessage);
	DDX_Text(pDX, IDC_STATIC_SHA1, m_static);
}


BEGIN_MESSAGE_MAP(Message_SHA1, CDialogEx)
	ON_BN_CLICKED(IDC_BNSHA, &Message_SHA1::OnBnClicked_SHA1)
END_MESSAGE_MAP()


// Message_SHA1 消息处理程序

/*************************************************************
 函数名称:	OnBnClicked_SHA1
 函数说明:	SHA1按钮
************************************************************/
void Message_SHA1::OnBnClicked_SHA1()
{
	GetDlgItemText(IDC_EDIT1, m_strMessage);
	if (!m_strMessage.IsEmpty()) {
		int err = 0;
		SHA1 sha;
		sha.Reset();
		USES_CONVERSION;									/*声明标识符（CString -> char*）*/
		char * message_array = T2A(m_strMessage);

		err = sha.Input(								/*SHA1 HASH操作	*/
			(const unsigned char *)message_array,
			strlen(message_array)
		);
		if (err) {
			m_static = _T("文件太大，超过 2^64 bit ( 2097152 TB )");
			GetDlgItem(IDC_STATIC_SHA1)->SetWindowText(m_static);
			return;
		}

		sha.Result(&m_static);

		MessageBox(_T("消息摘要已生成！"), _T("提示"), MB_OK);
		GetDlgItem(IDC_STATIC_SHA1)->SetWindowText(m_static);

		int nStatus = ((CButton*)GetDlgItem(IDC_CHECK1))->GetCheck();
		if (nStatus) {										/*输出到文件		*/
			char * message_dig = T2A(m_static);
			FILE *fp = NULL;
			fopen_s(&fp, "ASCII序列 SHA1摘要.txt", "w+");
			fprintf(fp, "%s", message_dig);
			fclose(fp);
		}
	}
	else {
		MessageBox(_T("请输入要生成摘要的ASCII序列！"), _T("提示"), MB_OK);
		return;
	}
}

