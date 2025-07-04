<template>
  <v-dialog v-model="isOpen" max-width="500px">
    <v-card>
      <v-card-title class="text-h6">회원가입</v-card-title>
      <v-card-text>
        <v-text-field
          v-model="userId"
          label="사용자 ID"
          type="number"
          required
        />
        <v-text-field
          v-model="userName"
          label="이름"
          required
        />
        <v-checkbox
          v-model="isKtCustomer"
          label="KT 고객입니다"
        />
      </v-card-text>
      <v-card-actions>
        <v-spacer />
        <v-btn color="primary" @click="register">가입</v-btn>
        <v-btn text @click="close">취소</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { ref, defineExpose } from 'vue'
import axios from 'axios'

const isOpen = ref(false)
const userId = ref(null)
const userName = ref('')
const isKtCustomer = ref(false)

function open() {
  isOpen.value = true
}
function close() {
  isOpen.value = false
  // 초기화
  userId.value = null
  userName.value = ''
  isKtCustomer.value = false
}

async function register() {
  try {
    const payload = {
      userId: Number(userId.value),
      userName: userName.value,
      isKtCustomer: isKtCustomer.value
    }

    const response = await axios.post(
      'http://localhost:8085/users/userregistration',
      payload
    )

    alert('회원가입 성공! 포인트 지급 완료')
    console.log(response.data)
    close()
  } catch (err) {
    console.error(err)
    alert('회원가입 실패')
  }
}

// 외부에서 open() 함수 사용할 수 있도록 노출
defineExpose({ open })
</script>
