import request from '@/utils/request'

export function getSysDictDataList(params) {
  return request({
    url: '/admin/sysDictData/getList',
    method: 'post',
    data: params
  })
}

export function getListByDictType(params) {
  return request({
    url: '/admin/sysDictData/getListByDictType',
    method: 'post',
    params
  })
}

export function getListByDictTypeList(params) {
  return request({
    url: '/admin/sysDictData/getListByDictTypeList',
    method: 'post',
    data: params
  })
}

export function addSysDictData(params) {
  return request({
    url: '/admin/sysDictData/add',
    method: 'post',
    data: params
  })
}

export function editSysDictData(params) {
  return request({
    url: '/admin/sysDictData/edit',
    method: 'post',
    data: params
  })
}

export function deleteBatchSysDictData(params) {
  return request({
    url: '/admin/sysDictData/deleteBatch',
    method: 'post',
    data: params
  })
}
