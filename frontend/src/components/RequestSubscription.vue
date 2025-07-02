<template>

    <v-card outlined>
        <v-card-title>
            RequestSubscription
        </v-card-title>

        <v-card-text>
            <Number label="SubscriberId" v-model="value.subscriberId" :editMode="editMode"/>
            <SubscriptionPlanType offline label="PlanType" v-model="value.planType" :editMode="editMode" @change="change"/>
        </v-card-text>

        <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn
                    color="deep-purple lighten-2"
                    text
                    @click="requestSubscription"
            >
                RequestSubscription
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
    name: 'RequestSubscriptionCommand',
    components:{
    },
    props: {},
    data: () => ({
        editMode: true,
        value: {},
    }),
    created() {
        this.value.subscriberId = 0;
        this.value.planType = {};
    },
    watch: {
    },
    methods: {
        requestSubscription() {
            this.$emit('requestSubscription', this.value);
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

