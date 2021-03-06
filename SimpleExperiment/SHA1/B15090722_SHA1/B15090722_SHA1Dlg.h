
// B15090722_SHA1Dlg.h: 头文件
//

#pragma once
#include "Message_SHA1.h"
#include "File_SHA1.h"


// CB15090722SHA1Dlg 对话框
class CB15090722SHA1Dlg : public CDialogEx
{
	// 构造
public:
	CB15090722SHA1Dlg(CWnd* pParent = nullptr);	// 标准构造函数
	CTabCtrl m_tab;
	int m_CurSelTab;
	Message_SHA1 m_mpage;
	File_SHA1 m_fpage;
	CDialog* pDialog[2];

	// 对话框数据
#ifdef AFX_DESIGN_TIME
	enum { IDD = IDD_B15090722_SHA1_DIALOG };
#endif

protected:
	virtual void DoDataExchange(CDataExchange* pDX);	// DDX/DDV 支持


// 实现
protected:
	HICON m_hIcon;

	// 生成的消息映射函数
	virtual BOOL OnInitDialog();
	afx_msg void OnSysCommand(UINT nID, LPARAM lParam);
	afx_msg void OnPaint();
	afx_msg HCURSOR OnQueryDragIcon();
	DECLARE_MESSAGE_MAP()
public:
	afx_msg void OnTcnSelchangeTab1(NMHDR *pNMHDR, LRESULT *pResult);
};
