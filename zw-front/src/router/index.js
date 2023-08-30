import { createRouter, createWebHistory } from 'vue-router';
import Home from '../pages/Home.vue';
import Learning from '../pages/Learning.vue';
import TopicEdit from '../pages/topic/TopicEdit.vue';
import TopicList from '../pages/topic/TopicList.vue';
import PointEdit from '../pages/point/PointEdit.vue';
import PointList from '../pages/point/PointList.vue';
import PracticeList from '../pages/practice/PracticeList.vue';
import Practice from '../pages/practice/Practice.vue';
import Login from '../pages/Login.vue';
import Register from '../pages/Register.vue';

const routes = [
    {
      path: '/',
      name: 'Home',
      component: Home
    },
    {
      path: '/learning',
      name: 'Learning',
      component: Learning
    },
    {
      path: '/topic',
      name: 'TopicEdit',
      component: TopicEdit,
      props: true
    },
    {
      path: '/topic/list',
      name: 'TopicList',
      component: TopicList
    },
    {
      path: '/point/list',
      name: 'PointList',
      component: PointList,
      props: true
    },
    {
      path: '/point',
      name: 'PointEdit',
      component: PointEdit,
      props: true
    },
    {
      path: '/practice',
      name: 'Practice',
      component: Practice
    },
    {
      path: '/login',
      name: 'Login',
      component: Login
    },
    {
      path: '/register',
      name: 'Register',
      component: Register
    },
    {
      path: '/practice/list',
      name: 'PracticeList',
      component: PracticeList
    },
  ];
  

  const router = createRouter({
    history: createWebHistory(),
    routes
  });
  
  export default router;