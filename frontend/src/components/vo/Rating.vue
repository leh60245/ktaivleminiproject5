<template>
    <div style="margin: 0 -15px 0 -15px;">
        <v-card-title v-if="editMode">
            {{label}}
        </v-card-title>
        <v-card-text v-if="value">
            <div v-if="editMode" style="margin-top:-20px;">
                <v-text-field type="number" label="Rate" v-model="value.rate" placeholder="1~5"/>
            </div>
            <div v-if="!editMode">
                <v-rating color="warning" readonly v-model="value.rate"></v-rating>
            </div>
        </v-card-text>
    </div>
</template>

<script>
    export default {
        name:"Rating",
        props: {
            editMode: Boolean,
            value : Object,
            label : String,
        },
        created(){
            this.value = this.modelValue
            if(!this.value) {
                this.value = {
                    'rate': 0,
                };
            }

        },
        watch: {
            value(newVal) {
                this.$emit('update:modelValue', newVal);
            },
        },
    }
</script>

<style scoped>
</style>