const path = require('path');

const MANIFEST = path.join(__dirname, 'dist', 'manifest.json');
const RESOURCES_DIR = path.join(
  __dirname,
  'dist',
  'etc.clientlibs',
  'aem-vite-demo',
  'clientlibs',
  'clientlib-esmodule',
  'resources'
);

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
  'clientlibs',
  'clientlib-esmodule'
);

module.exports = {
  libs: [
    {
      manifest: MANIFEST,
      resourcesDir: RESOURCES_DIR,
      clientlibDir: CLIENTLIB_DIR,
      categories: ['aem-vite-demo.esmodule'],
      properties: {
        moduleIdentifier: 'vite',
      },
    },
  ],
};
