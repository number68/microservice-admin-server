import javamelodyEndpoint from './javamelody-endpoint';

SBA.use({
  install({viewRegistry}) {
    viewRegistry.addView({
      name: 'instances/javamelody',
      parent: 'instances',
      path: 'javamelody',
      component: javamelodyEndpoint,
      label: 'Javamelody',
      order: 1000
    });
  }
});