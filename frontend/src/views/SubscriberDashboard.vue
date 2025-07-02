<template>
  <div class="pa-6">
    <h2>📖 구독자 대시보드</h2>

    <v-card class="mt-4">
      <v-card-title>보유 포인트: {{ points }}P</v-card-title>
      <v-card-text>
        <v-btn color="success" @click="$router.push('/subscriber/read')">
          📚 도서 열람하러 가기
        </v-btn>
      </v-card-text>
    </v-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const points = ref(6000) // KT 고객일 경우 추가 포인트라고 가정
</script>
