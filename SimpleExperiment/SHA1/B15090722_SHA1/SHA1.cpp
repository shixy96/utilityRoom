#include "stdafx.h"
#include "SHA1.h"

SHA1::SHA1()
{
}

SHA1::~SHA1()
{
}

/*
32位Dword 循环左移 bits 位
*/
#define CirculatoryLeftShift(Dword,bits) (((Dword) << (bits)) | ((Dword) >> (32-(bits))))

/********************************************************************
函数名称:	Reset
函数说明:   在对新的消息进行 SHA1 HASH 处理前对 SHA1 进行的初始化
********************************************************************/
void SHA1::Reset()
{
	Hash_Register[0] = 0x67452301;				/*寄存器初始化*/
	Hash_Register[1] = 0xEFCDAB89;
	Hash_Register[2] = 0x98BADCFE;
	Hash_Register[3] = 0x10325476;
	Hash_Register[4] = 0xC3D2E1F0;

	Length_Low = 0;								/*状态初始化*/
	Length_High = 0;
	Message_Block_Index = 0;
	IsTooLong = 0;
}

/********************************************************************
函数名称:	Input
函数说明：
*    对长度不超过 2^64 bit 的 8字节 消息数组 message_array 进行处理：
*    将输入消息以 512 bit 为分组单位进行分组处理，
*	 即，对满 512 bit 的消息分组进行 HASH 处理
*    剩下的不足 512 bit 的消息分组留待 Result 函数处理
********************************************************************/
int SHA1::Input(const uint8_t *message_array, unsigned length) {
	if (!length) {
		return Success;
	}

	if (IsTooLong) {
		return IsTooLong;
	}

	while (length-- && !IsTooLong) {
		Message_Block[Message_Block_Index++] =
			(*message_array & 0xFF);

		Length_Low += 8;
		if (Length_Low == 0) {
			Length_High++;
			if (Length_High == 0)
			{									/* 消息长度大于2^64 bit */
				IsTooLong = 1;
				return IsTooLong;
			}
		}
		if (Message_Block_Index == 64) {		/*当消息块长度达到 512 bit 时，*/
			Compression();						/*进行一次SHA1ProcessMessageBlock分组处理，*/
		}										/*	在 SHA1ProcessMessageBlock 函数中 Message_Block_Index 置 0*/

		message_array++;
	}
	return Success;
}

/********************************************************************
函数名称:	Result
函数说明：
*     如果还有不满 512 bit 的消息分组，则对该分组进行填充处理；
*	  然后，将存储在成员变量Hash_Register中的消息摘要赋给 m_static 以显示
*
********************************************************************/
int SHA1::Result(CString *m_static) {
	int i;

	if (IsTooLong) {
		return IsTooLong;
	}

	PadMessage();
	for (i = 0; i < 64; ++i) {					/*完成后清理Message_Block中消息内容*/
		Message_Block[i] = 0;
	}
	Length_Low = 0;
	Length_High = 0;

	uint8_t tmp;								/* 将寄存器中的摘要赋值给传过来的 m_static */
	if (!m_static->IsEmpty())
		m_static->Empty();
	for (i = 0; i < 20; ++i) {
		tmp = Hash_Register[i >> 2] >> 8 * (3 - (i & 0x03));
		m_static->AppendFormat(_T("%02X "), tmp);
	}
	return Success;
}

/********************************************************************
*	非线性函数 NonLinearFunction ( i, B, C, D )
*	参数：轮数 i, 寄存器值B, C, D
********************************************************************/
uint32_t NonLinearFunction(int i, uint32_t B, uint32_t C, uint32_t D) {
	if (i == 0)	return ((B & C) ^ ((~B) & D));
	if (i == 1)	return (B ^ C ^ D);
	if (i == 2)	return ((B & C) ^ (B & D) ^ (C & D));
	if (i == 3)	return (B ^ C ^ D);
}

/********************************************************************
函数名称:	Compression
函数说明：
*   将 SHA1 中的 512bit 字节块 Message_Block 进行
*   一次分组处理（四轮，每轮20步）
********************************************************************/
void SHA1::Compression() {
	const uint32_t K[] = {						/* 轮常量Kr */
		0x5A827999,
		0x6ED9EBA1,
		0x8F1BBCDC,
		0xCA62C1D6
	};

	int           t;							/* 循环次数		     */
	uint32_t      tmp;							/* 赋值给A的中间值	 */
	uint32_t      W[80];						/* 32bit 消息字     */
	uint32_t      A, B, C, D, E;				/* 五个32bit寄存器  */

	for (t = 0; t < 16; t++) {					/*W0--W15直接由512bit消息分组得到*/
		W[t] = Message_Block[t * 4] << 24;
		W[t] |= Message_Block[t * 4 + 1] << 16;
		W[t] |= Message_Block[t * 4 + 2] << 8;
		W[t] |= Message_Block[t * 4 + 3];
	}

	for (t = 16; t < 80; t++) {					/*W16--W79异或循环得到*/
		W[t] = CirculatoryLeftShift(W[t - 3] ^ W[t - 8] ^ W[t - 14] ^ W[t - 16], 1);
	}

	A = Hash_Register[0];
	B = Hash_Register[1];
	C = Hash_Register[2];
	D = Hash_Register[3];
	E = Hash_Register[4];						/*4轮，每轮20步，每轮 非线性函数 和 Kr 不同*/

	for (t = 0; t < 80; t++) {					/*每步消息字 Wt 不同						*/
		tmp = CirculatoryLeftShift(A, 5) + NonLinearFunction(t / 20, B, C, D) + E + W[t] + K[t / 20];
		E = D;
		D = C;
		C = CirculatoryLeftShift(B, 30);
		B = A;
		A = tmp;
	}

	Hash_Register[0] += A;						/*最后模32加上此次迭代的输入CV*/
	Hash_Register[1] += B;
	Hash_Register[2] += C;
	Hash_Register[3] += D;
	Hash_Register[4] += E;

	Message_Block_Index = 0;					/*Message_Block_Index 复位*/
}

/********************************************************************
函数名称:	PadMessage
函数说明：
*    当消息分组长度不足512bit时，进行填充（1 + 若干0 + 64bit消息长度）
*    并进行SHA1 HASH 压缩处理
********************************************************************/
void SHA1::PadMessage() {
	if (Message_Block_Index > 55) {				/*当剩余控件小于64 bit（8 bytes），填充至下一个消息块*/
		Message_Block[Message_Block_Index++] = 0x80;
		while (Message_Block_Index < 64) {
			Message_Block[Message_Block_Index++] = 0;
		}

		Compression();							/*将该分组先进行压缩处理*/
	}
	else {
		Message_Block[Message_Block_Index++] = 0x80;
	}
	while (Message_Block_Index < 56) {			/*填充至剩余 64 bit (8 bytes)*/
		Message_Block[Message_Block_Index++] = 0;
	}

	Message_Block[56] = Length_High >> 24;		/*最后 64bit(8 bytes) 填入消息块长度*/
	Message_Block[57] = Length_High >> 16;
	Message_Block[58] = Length_High >> 8;
	Message_Block[59] = Length_High;
	Message_Block[60] = Length_Low >> 24;
	Message_Block[61] = Length_Low >> 16;
	Message_Block[62] = Length_Low >> 8;
	Message_Block[63] = Length_Low;

	Compression();								/*将该分组进行压缩处理*/
}
