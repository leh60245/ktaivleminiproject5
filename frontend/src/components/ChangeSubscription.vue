<template>

    <v-card outlined>
        <v-card-title>
            ChangeSubscription
        </v-card-title>

        <v-card-text>
            <Number label="UserId" v-model="value.userId" :editMode="editMode"/>
            <Boolean label="IsSubscription" v-model="value.isSubscription" :editMode="editMode"/>
        </v-card-text>

        <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn
                    color="deep-purple lighten-2"
                    text
                    @click="changeSubscription"
            >
                ChangeSubscription
            </v-btn>
            
            <v-btn
                    color="deep-purple lighten-2"
                    text
                    @click="close"
            >
                Close
            </v-btn>
        </v-card-actions>
    </v-card>

</template>

<script>

export default {
    name: 'ChangeSubscriptionCommand',
    components:{
    },
    props: {},
    data: () => ({
        editMode: true,
        value: {},
    }),
    created() {
        this.value.userId = 0;
        this.value.isSubscription = false;
    },
    watch: {
    },
    methods: {
        changeSubscription() {
            this.$emit('changeSubscription', this.value);
        },
        close() {
            this.$emit('closeDialog');
        },
        change() {
            this.$emit("update:modelValue", this.value);
        },
    }
}
</script>

