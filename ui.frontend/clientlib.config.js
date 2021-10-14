const path = require('path');

const BUILD_DIR = path.join(__dirname, 'dist');
const CLIENTLIB_DIR = path.join(
  __dirname,
  '..',
  'ui.apps',
  'src',
  'main',
  'content',
  'jcr_root',
  'apps',
  'aem-vite-demo',
  'clientlibs'
);

const libsBaseConfig = {
  allowProxy: true,
  serializationFormat: 'xml',
  cssProcessor: ['default:none', 'min:none'],
  jsProcessor: ['default:none', 'min:none'],
};

module.exports = {
  context: BUILD_DIR,
  clientLibRoot: CLIENTLIB_DIR,
  libs: [
    {
      ...libsBaseConfig,
      customProperties: ['moduleIdentifier'],
      moduleIdentifier: 'vite',
      name: 'clientlib-esmodule',
      categories: ['aem-vite-demo.esmodule'],
      assets: {
        resources: {
          cwd: 'aem-vite-demo.esmodule',
          files: ['**/*.*'],
          flatten: false,
        },
      },
    },
    {
      ...libsBaseConfig,
      customProperties: ['moduleIdentifier'],
      moduleIdentifier: 'vite',
      name: 'clientlib-esmodule-other',
      categories: ['aem-vite-demo.esmodule.other'],
      assets: {
        resources: {
          cwd: 'aem-vite-demo.esmodule.other',
          files: ['**/*.*'],
          flatten: false,
        },
      },
    },
  ],
};
