#pragma once


// Message_SHA1 对话框

class Message_SHA1 : public CDialogEx
{
	DECLARE_DYNAMIC(Message_SHA1)

public:
	Message_SHA1(CWnd* pParent = nullptr);   // 标准构造函数
	virtual ~Message_SHA1();

	// 对话框数据
#ifdef AFX_DESIGN_TIME
	enum { IDD = IDD_DIALOG1 };
#endif

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV 支持

	DECLARE_MESSAGE_MAP()
public:
	CString m_strMessage;					//ASCII序列
	CString m_static;					    //SHA1摘要
	afx_msg void OnBnClicked_SHA1();		//SHA1按钮点击函数
};
