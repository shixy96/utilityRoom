#pragma once


// File_SHA1 对话框

class File_SHA1 : public CDialogEx
{
	DECLARE_DYNAMIC(File_SHA1)

public:
	File_SHA1(CWnd* pParent = nullptr);   // 标准构造函数
	virtual ~File_SHA1();

	// 对话框数据
#ifdef AFX_DESIGN_TIME
	enum { IDD = IDD_DIALOG2 };
#endif

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV 支持

	DECLARE_MESSAGE_MAP()
public:
	CString m_strFilePath;						//文件路径
	CString m_static;							//SHA1摘要
	afx_msg void OnDropFiles(HDROP hDropInfo);	//文件拖拽函数
	afx_msg void OnBnClickedBnfile();			//文件选择按钮点击函数
	afx_msg void OnBnClickedBnsha();			//SHA1按钮点击函数
};
