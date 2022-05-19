export default {
  setCatalogs(state, catalogs) {
    state.catalogs = catalogs
  },
  setArchives(state, archives) {
    state.archives = archives
  },
  setSort(state, sort) {
    state.sort = sort
  },
  setTags(state, tags) {
    state.tags = tags
  },
  setNewComments(state, newComments) {
    state.newComments = newComments
  },
  setNewBlogs(state, newblogs) {
    state.newblogs = newblogs
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
  }
}
