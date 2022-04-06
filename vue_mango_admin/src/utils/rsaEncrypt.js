import JSEncrypt from 'jsencrypt/bin/jsencrypt.min'

// 密钥对生成 http://web.chacuo.net/netrsakeypair

const publicKey = 'MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDJLU9LXdl8yHnHymWXDQ6FUty5bi991wq/DN/pDEU+iagtCsNet9gVTnSupNKqXM5B8H0g8PdV9kZZnIPgpNPo4QHmZWLz+fO3+euEugHKKzYcwV0ZtKzr6DSQt0b7+lUS8YCVndNw5ZDKbZKeowzKJKSE80QppkrgJh44hyOCQQIDAQAB'

// 加密
export function encrypt(txt) {
  const encryptor = new JSEncrypt()
  encryptor.setPublicKey(publicKey) // 设置公钥
  return encryptor.encrypt(txt) // 对需要加密的数据进行加密
}

