const path = require('path');

const buildManifest = (name) => {
  return path.join(__dirname, 'dist', name, 'manifest.json');
};

const buildResourcesDir = (name) => {
  return path.join(
    __dirname,
    'dist',
    name,
    'etc.clientlibs',
    'aem-vite-demo',
    'clientlibs',
    name,
    'resources'
  );
};

const buildClientlibDir = (name) => {
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
    name
  );
};

const createLib = (name, categories) => {
  return {
    manifest: buildManifest(name),
    resourcesDir: buildResourcesDir(name),
    clientlibDir: buildClientlibDir(name),
    categories: [...categories],
    properties: {
      moduleIdentifier: 'vite',
    },
  };
};

module.exports = {
  libs: [
    createLib('clientlib-esmodule', ['aem-vite-demo.esmodule']),
    createLib('clientlib-esmodule-another', ['aem-vite-demo.esmodule.another']),
  ],
};
