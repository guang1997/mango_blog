import Vue from 'vue'
import { getDetailsByDictName } from '@/api/system/dictDetail'

export default class Dict {
  constructor(dict) {
    this.dict = dict
  }

  async init(names, completeCallback) {
    if (names === undefined || name === null) {
      throw new Error('need Dict names')
    }
    const ps = []
    names.forEach(n => {
      Vue.set(this.dict.dict, n, {})
      Vue.set(this.dict.label, n, {})
      Vue.set(this.dict, n, [])
      ps.push(getDetailsByDictName(n).then(res => {
        this.dict[n].splice(0, 0, ...res.data.data)
        res.data.data.forEach(d => {
          Vue.set(this.dict.dict[n], d.dictValue, d)
          Vue.set(this.dict.label[n], d.dictValue, d.dictLabel)
        })
      }))
    })
    await Promise.all(ps)
    completeCallback()
  }
}
