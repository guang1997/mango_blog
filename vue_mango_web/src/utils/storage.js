/**
 * @desc   localStorage信息存储
 * @author Justjokee
 */

// 访客信息key
const visitorKey = 'mango_blog_visitor_key'

export const storage = {
  getVisitor() {
    if (localStorage.getItem(visitorKey)) {
      return JSON.parse(localStorage.getItem(visitorKey))
    }
  },
  setVisitor(info) {
    localStorage.setItem(visitorKey, JSON.stringify(info))
  },
  removeVisitor() {
    localStorage.removeItem(visitorKey)
  }
}
