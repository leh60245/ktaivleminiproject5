<template>
  <section class="pa-6">
    <h2 class="text-h4 mb-6">📚 전체 도서 보기</h2>

    <v-row dense>
      <v-col
        v-for="(book, index) in books"
        :key="index"
        cols="12" sm="6" md="4" lg="3"
      >
        <v-card
          class="pa-2"
          elevation="2"
          @click="goToDetail(book)"
          style="cursor: pointer;"
        >
          <v-img
            :src="book.coverImage || defaultImage"
            height="200"
            cover
            class="mb-2"
          />
          <v-card-title class="text-h6">{{ book.title }}</v-card-title>
          <v-card-subtitle class="text-subtitle-2">
            👤 {{ book.author }}
          </v-card-subtitle>
          <v-card-text class="text-truncate">
            {{ book.content }}
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </section>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

// 샘플 도서 데이터
const books = ref([
  {
    title: 'AI 시대의 독서',
    author: '홍길동',
    content: 'AI가 바꾸는 독서의 패러다임을 설명하는 책입니다.',
    coverImage: 'https://via.placeholder.com/200x300.png?text=AI+Book'
  },
  {
    title: 'Vue 완전 정복',
    author: '김개발',
    content: 'Vue.js를 기초부터 마스터하는 실전 가이드북입니다.',
    coverImage: 'https://via.placeholder.com/200x300.png?text=Vue+Book'
  }
])

const defaultImage = '/default-cover.png'

// 도서 클릭 시 상세 페이지 이동 (라우터 이름은 BookDetail로 가정)
function goToDetail(book) {
  router.push({ name: 'BookDetail', params: { title: book.title } })
}

// 로컬스토리지에 저장 (옵션)
onMounted(() => {
  localStorage.setItem('writtenPosts', JSON.stringify(books.value))
})
</script>

<style scoped>
.text-truncate {
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}
</style>
