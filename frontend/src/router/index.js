import { createRouter, createWebHistory } from 'vue-router'

// 페이지 컴포넌트 불러오기
import WriterDashboard from '@/views/WriterDashboard.vue'
import WriteContent from '@/views/WriteContent.vue'
import AiPreview from '@/views/AiPreview.vue'
import SubscriberDashboard from '@/views/SubscriberDashboard.vue'
import ReadBook from '@/views/ReadBook.vue'

const routes = [
  {
    path: '/',
    redirect: '/books'   // ✅ 메인화면을 기본 진입 경로로 설정
  },
  {
    path: '/books',
    name: 'Books',
    component: () => import('@/views/Main.vue')
  },
  {
  path: '/books/:title',
  name: 'BookDetail',
  component: () => import('@/views/BookDetail.vue')
  },
  {
    path: '/writer',
    name: 'WriterDashboard',
    component: WriterDashboard
  },
  {
    path: '/writer/write',
    name: 'WriteContent',
    component: WriteContent
  },
  {
    path: '/ai-preview',
    name: 'AiPreview',
    component: AiPreview
  },
  {
    path: '/subscriber',
    name: 'SubscriberDashboard',
    component: SubscriberDashboard
  },
  {
    path: '/subscriber/read',
    name: 'ReadBook',
    component: ReadBook
  },
  {
    path: '/writer/edit',
    name: 'EditContent',
    component: () => import('@/views/EditContent.vue')
  }
]

// ✅ 이 부분 추가!
const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
