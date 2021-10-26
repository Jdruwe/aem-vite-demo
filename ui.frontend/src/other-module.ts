import './other-styling.css';

console.log('>>> other module loaded');

document.getElementById('green').addEventListener('click', () => {
  import('./nested-module');
});
