import request from '@/utils/request'

export function sendCode(data) {
    return request({
      url: '/web/user/sendCode',
      method: 'post',
      data
    })
  }
  
  
  export default { sendCode }