import request from '@/utils/request'

export function getServers() {
  return request({
    url: '/admin/monitor/getServers',
    method: 'get'
  })
}
export default { getServers }