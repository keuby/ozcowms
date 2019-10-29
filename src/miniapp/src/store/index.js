import Vuex from '@wepy/x'
import config from '@/config'
import { noop } from '@/utils/common'

const app = getApp()

export default new Vuex.Store({
  state: {
    title: config.nav.pages[0].title,
    currentPage: config.nav.pages[0],
    contentHeight: 0,
    navHeight: 0,
    headerHeight: 0,
    storages: [],
    contacts: null,
    milkPowderUnits: []
  },
  mutations: {
    setTitle (state, title) {
      state.title = title
    },
    setStorages (state, storages) {
      state.storages = storages
    },
    setContacts(state, contacts) {
      state.contacts = contacts
    },
    setCurrentPage(state, page) {
      state.currentPage = page
    },
    setContentHeight(state, height) {
      state.contentHeight = height
    },
    setNavHeight(state, height) {
      state.navHeight = height
    },
    setHeaderHeight(state, height) {
      state.headerHeight = height
    },
    setMilkPowderUnits(state, units) {
      state.milkPowderUnits = units
    }
  },
  actions: {
    /**
     * 切换到底部导航栏菜单页面
     */
    switchTo ({ commit }, page) {
      commit('setTitle', page.title)
      commit('setCurrentPage', page)
    },
    async updateStorages ({ commit, state }, { force = false, loading = noop } = {}) {
      if (!force && state.storages.length > 0) return
      loading()
      let resp = await app.$wepy.request.get('/product/milk-powder/detail', {
        category: 'milk-powder'
      })
      if (resp.ok) commit('setStorages', resp.data)
    },
    async updateContacts ({ commit, state }, { force = false, loading = noop } = {}) {
      if (!force && state.contacts) return
      loading()
      let resp = await app.$wepy.request.get('/user-center/contacts')
      if (!resp.ok) throw new Error(resp.msg)
      commit('setContacts', resp.data)
    },
    async initMilkPowderUnits({ commit, state }, { force = false, loading = noop } = {}) {
      if (!force && state.milkPowderUnits.length > 0) return
      loading()
      let resp = await app.$wepy.request.get('/product/unit/milk-powder')
      if (resp.ok) commit('setMilkPowderUnits', resp.data)
    }
  }
})

