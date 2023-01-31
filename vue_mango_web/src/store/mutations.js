export default {
  setCatalogs(state, catalogs) {
    state.catalogs = catalogs
  },
  setArchives(state, archives) {
    state.archives = archives
  },
  setSorts(state, sorts) {
    state.sorts = sorts
  },
  setTags(state, tags) {
    state.tags = tags
  },
  setNewComments(state, newComments) {
    state.newComments = newComments
  },
  setNewBlogs(state, newBlogs) {
    state.newBlogs = newBlogs
  },
  setActiveCatalog(state, id) {
    state.activeCatalog = id
  },
  setRollBack(state, rollBack) {
    state.rollBack = rollBack
  },
  setVisitor(state, info) {
    state.visitorInfo = info
  },
  setTotals(state, kv) {
    state.totals[kv.key] = kv.value
  },
  setWebConfig(state, webConfig) {
    state.webConfig = webConfig
  }
}
