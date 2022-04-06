import request from '@/utils/request'

  export function sendCode(form) {
    return request({
      url: '/admin/code/sendCode?email=' + form.email,
      method: 'get'
    })
  }
export default { sendCode }