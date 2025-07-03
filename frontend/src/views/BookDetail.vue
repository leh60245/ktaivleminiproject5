<template>
  <section class="pa-6">
    <!-- 뒤로 가기 버튼 -->
    <v-btn icon @click="goBack" class="mb-4">
      <v-icon>mdi-arrow-left</v-icon>
    </v-btn>

    <h2 class="text-h4 mb-4">📖 도서 상세 정보</h2>

    <!-- 도서가 존재할 경우 -->
    <v-card v-if="book" class="pa-4" elevation="2">
      <v-row>
        <v-col cols="12" md="4">
          <v-img :src="book.coverImage || defaultImage" height="300" cover />
        </v-col>
        <v-col cols="12" md="8">
          <h3 class="text-h5">{{ book.title }}</h3>
          <p class="text-subtitle-1">👤 {{ book.author }}</p>
          <p class="mt-4">{{ book.content }}</p>
        </v-col>
      </v-row>
    </v-card>

    <!-- 도서가 없을 경우 -->
    <v-alert v-else type="error" color="error" variant="tonal">
      ❌ 해당 도서를 찾을 수 없습니다. <br />
      <v-btn color="primary" class="mt-2" @click="goToMain">전체 도서 보기로 이동</v-btn>
    </v-alert>
  </section>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

const book = ref(null)
const defaultImage = '/default-cover.png'

function goBack() {
  router.back()
}

function goToMain() {
  router.push('/books')
}

onMounted(() => {
  const titleParam = decodeURIComponent(route.params.title || '')

  try {
    const stored = localStorage.getItem('writtenPosts')
    if (!stored) return

    const parsed = JSON.parse(stored)
    const found = parsed.find(b => b.title === titleParam)

    if (found) {
      book.value = found
    } else {
      console.warn('책 제목에 해당하는 도서 없음:', titleParam)
    }
  } catch (err) {
    console.error('localStorage 파싱 실패:', err)
  }
})
</script>
