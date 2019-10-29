export default {
  nav: {
    class: {
      active: 'text-blue',
      disactive: 'text-gray'
    },
    pages: [{
      name: 'milk-powder',
      icon: {
        active: 'cuIcon-goodsfill',
        disactive: 'cuIcon-goods',
      },
      title: '奶粉'
    }, {
      name: 'friend',
      icon: {
        active: 'cuIcon-friendfill',
        disactive: 'cuIcon-friend'
      },
      title: '联系人'
    }
    // , {
    //   name: 'settings',
    //   icon: {
    //     active: 'cuIcon-settingsfill',
    //     disactive: 'cuIcon-settings'
    //   },
    //   title: '设置'
    // }
  ]},
  pages: {
    milkPowderEdit: 'pages/milk-powder/edit',
    milkPowderDetail: 'pages/milk-powder/detail'
  }
}