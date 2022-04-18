import request from '@/utils/request'

export function init() {
  return request({
    url: '/admin/dashboard/init',
    method: 'get'
  })
}

export function getVisitByWeek() {
  return request({
    url: '/admin/dashboard/getVisitByWeek',
    method: 'get'
  })
}

export function getBlogCountByTag() {
  return request({
    url: '/admin/dashboard/getBlogCountByTag',
    method: 'get'
  })
}

export function getBlogCountByBlogSort() {
  return request({
    url: '/admin/dashboard/getBlogCountByBlogSort',
    method: 'get'
  })
}


export function getBlogContributeCount() {
  return request({
    url: '/admin/dashboard/getBlogContributeCount',
    method: 'get'
  })
}
export function getWebVisitGroupByBehavior() {
  return request({
    url: '/admin/dashboard/getWebVisitGroupByBehavior',
    method: 'get'
  })
}

export default { init, getVisitByWeek, getBlogCountByTag, getBlogCountByTag, getBlogCountByBlogSort, getBlogContributeCount, getWebVisitGroupByBehavior}