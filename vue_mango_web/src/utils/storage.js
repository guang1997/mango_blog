/**
 * @desc   localStorage信息存储
 * @author Justjokee
 */

// 访客信息key
const visitorKey = 'mango_blog_visitor_key'
const mangoBlogBrowserFingerKey = 'mango_blog_browser_finger_key'
const mangoBlogScreenInformationKey = 'mango_blog_screen_information'

export const storage = {
  // 用户信息
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
  },

    // 浏览器指纹
  getMangoBlogBrowserFinger() {
    if (localStorage.getItem(mangoBlogBrowserFingerKey)) {
      return JSON.parse(localStorage.getItem(mangoBlogBrowserFingerKey))
    }
  },
  setMangoBlogBrowserFinger(info) {
    localStorage.setItem(mangoBlogBrowserFingerKey, JSON.stringify(info))
  },
  removeMangoBlogBrowserFinger() {
    localStorage.removeItem(mangoBlogBrowserFingerKey)
  },

  // 分辨率
  getMangoBlogScreenInformation() {
    if (localStorage.getItem(mangoBlogScreenInformationKey)) {
      return JSON.parse(localStorage.getItem(mangoBlogScreenInformationKey))
    }
  },
  setMangoBlogScreenInformation(info) {
    localStorage.setItem(mangoBlogScreenInformationKey, JSON.stringify(info))
  },
  removeMangoBlogScreenInformation() {
    localStorage.removeItem(mangoBlogScreenInformationKey)
  }
}
