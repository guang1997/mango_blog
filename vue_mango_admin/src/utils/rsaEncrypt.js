import JSEncrypt from 'jsencrypt/bin/jsencrypt.min'

// 密钥对生成 http://web.chacuo.net/netrsakeypair

const publicKey = 'MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCQ6Jw6ODngPj59DDLh7JDcbbR1UKgXCcwsEwGV2U26j2mYDR0rK1S6x+a2WfaSjo21R9UCqhF4YCiVtYzgaKaVoBbyqlG0skLFUFHkvsh/W0tdIeTYYpoEO/8IGL7u7GRC1IkPbwQkqm69n7RducATv4khvuRN8y9OvAbLQO3vwQIDAQAB'

// 加密
export function encrypt(txt) {
  const encryptor = new JSEncrypt()
  encryptor.setPublicKey(publicKey) // 设置公钥
  return encryptor.encrypt(txt) // 对需要加密的数据进行加密
}

