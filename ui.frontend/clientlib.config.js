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
      name: 'clientlib-vite',
      categories: ['aem-vite.base'],
      customProperties: ['esModule'],
      esModule: true,
      assets: {
        // Copy entrypoint scripts and stylesheets into the respective ClientLib
        // directories
        js: {
          base: 'resources/js',
          cwd: 'clientlib-vite',
          files: ['**/*.js'],
          flatten: true,
        },
        css: {
          base: 'resources/css',
          cwd: 'clientlib-vite',
          files: ['**/*.css'],
          flatten: true,
        },
      },
    },
    // {
    //   ...libsBaseConfig,
    //   customProperties: ['esModule'],
    //   esModule: true,
    //   name: 'clientlib-vite',
    //   categories: ['aem-vite.base'],
    //   assets: {
    //     resources: {
    //       cwd: 'aem-vite.base',
    //       files: ['**/*.*'],
    //       flatten: false,
    //     },
    //   },
    // },
  ],
};
