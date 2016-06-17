Ext.define('kalix.orgchart.view.OrgChart', {
  extend: 'Ext.container.Container',
  requires: [
    'kalix.orgchart.lib.JitLib',
    'kalix.orgchart.controller.OrgChartController',
    'kalix.orgchart.viewModel.OrgChartViewModel'
  ],
  controller: 'orgChartController',
  layout: 'absolute',
  xtype: 'orgChart',
  viewModel: 'orgChartViewModel',
  //--[org chart extend begin]
  orgTree: null,
  autoLoad: false,
  jsonData: null,
  //--[org chart extend end]
  listeners: {
    afterrender: 'orgChartAfterRender'
  },
  items: [
    {
      xtype: 'container',
      listeners: {
        afterrender: 'orgChartContainerAfterRender'
      }
    },
    {
      xtype: 'label',
      bind: {
        text: '{level}'
      },
      x: 63,
      y: 55,
      style:'font-size:16px'
    },
    {
      xtype: 'container',
      x: 54,
      y: 130,
      width:20,
      items: [
        {
          xtype:'button',
          width:16,
          height:16,
          margin:'0 0 3 3',
          //style:'background:white',
          bind:{
            tooltip:'{toggleTip}'
          },
          enableToggle:true,
          toggleHandler:'buttonToggle'
        },
        {
          xtype: 'slider',
          height: 200,
          bind: {
            value: '{level}'
          },
          increment: 1,
          minValue: 1,
          maxValue: 5,
          vertical: true,
          listeners:{
            change:'levelChange'
          }
        }
      ]
    },
    {
      xtype: 'image',
      src: 'orgchart/resources/images/arrow_blue_up.png',
      x: 50,
      y: 10,
      alt: 'bottom',
      style: 'cursor:pointer',
      listeners: {
        afterrender: 'imageAfterRender'
      }
    },
    {
      xtype: 'image',
      src: 'orgchart/resources/images/arrow_blue_right.png',
      x: 82,
      y: 45,
      alt: 'left',
      style: 'cursor:pointer',
      listeners: {
        afterrender: 'imageAfterRender'
      }
    },
    {
      xtype: 'image',
      src: 'orgchart/resources/images/arrow_blue_down.png',
      x: 50,
      y: 80,
      alt: 'top',
      style: 'cursor:pointer',
      listeners: {
        afterrender: 'imageAfterRender'
      }
    },
    {
      xtype: 'image',
      src: 'orgchart/resources/images/arrow_blue_left.png',
      x: 18,
      y: 45,
      alt: 'right',
      style: 'cursor:pointer',
      listeners: {
        afterrender: 'imageAfterRender'
      }
    }
  ]
});
