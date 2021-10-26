const path = require('path');

const buildResourcesDir = (category) => {
  return path.join(
    __dirname,
    'dist',
    category,
    'etc.clientlibs',
    'aem-vite-demo',
    'clientlibs',
    category,
    'resources'
  );
};

const buildClientlibDir = (category) => {
  return path.join(
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
    category
  );
};

//TODO: create a function to create new libs

module.exports = {
  libs: [
    {
      manifest: path.join(
        __dirname,
        'dist',
        'clientlib-esmodule',
        'manifest.json'
      ),
      resourcesDir: buildResourcesDir('clientlib-esmodule'),
      clientlibDir: buildClientlibDir('clientlib-esmodule'),
      categories: ['aem-vite-demo.esmodule'],
      properties: {
        moduleIdentifier: 'vite',
      },
    },
    {
      manifest: path.join(
        __dirname,
        'dist',
        'clientlib-esmodule-another',
        'manifest.json'
      ),
      resourcesDir: buildResourcesDir('clientlib-esmodule-another'),
      clientlibDir: buildClientlibDir('clientlib-esmodule-another'),
      categories: ['aem-vite-demo.esmodule.another'],
      properties: {
        moduleIdentifier: 'vite',
      },
    },
  ],
};
