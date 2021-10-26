import './styling.css';
import { createApp } from 'vue';
import Counter from './components/counter/Counter.vue';

createApp(Counter).mount('counter');

document.getElementById('yellow').addEventListener('click', () => {
  import('./other-module');
});
