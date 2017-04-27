Ext.define('kalix.view.Profilebar', {
  extend: 'Ext.toolbar.Toolbar',
  xtype: 'profilebar',

  requires: [
    'kalix.viewmodel.ProfilebarViewModel',
    'kalix.view.MutiIconButton'
  ],

  viewModel: 'profilebarViewModel',

  itemId: 'profilebar',

  layout: {
    type: 'hbox'
  },

  border: false,

  items: [
    {
      xtype: 'mutiiconbutton',
      bind: {
        text: '{user.name}',
        icon:['{user.icon}',CONFIG.restRoot+'/resources/images/default_user.png']
      },
      iconCls: 'custom-user-icon',
      margin: '0 5 0 10',
      menu: {
        items: [
          {
            text: '修改个人信息',
            iconCls: 'iconfont icon-edit-column',
            handler: 'onUpdateUserInfo'
          },
          {
            text: '修改密码',
            iconCls: 'iconfont icon-password-reset',
            handler: 'onUpdateUserPassword'
          }
        ]
      }
    },
    {
      xtype: 'button',
      text: '退出',
      iconCls: 'iconfont icon-exit',
      handler:'onLogout',
      // bind: {
      //   href: '{user.quit}'
      // },
      // hrefTarget: '_self',
      margin: '0 5 0 10'
    }
  ]
});
