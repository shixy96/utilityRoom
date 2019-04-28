#pragma once
#include <stdint.h>

#define Success 0

class SHA1
{
private:
	uint32_t		Hash_Register[5];		 /* 5个32位寄存器A,B,C,D,E*/
	uint32_t		Length_Low;				 /* 数据长度低位(32 bit)  */
	uint32_t		Length_High;			 /* 数据长度高位(32 bit)  */
	int_least16_t	Message_Block_Index;	 /* 消息块数组索引		 */
	uint8_t			Message_Block[64];		 /* 512-bit 消息块		 */

	int				IsTooLong;				 /* 消息是否过长(>2^64 bits)？ */

public:
	SHA1();
	~SHA1();
	void Reset();
	int Input(const uint8_t *message_array, unsigned length);
	int Result(CString *m_static);
	void PadMessage();
	void Compression();
};
