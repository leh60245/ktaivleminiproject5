<template>
    <v-container>
        <v-snackbar
            v-model="snackbar.status"
            :timeout="snackbar.timeout"
            :color="snackbar.color"
        >
            
            <v-btn style="margin-left: 80px;" text @click="snackbar.status = false">
                Close
            </v-btn>
        </v-snackbar>
        <div class="panel">
            <div class="gs-bundle-of-buttons" style="max-height:10vh;">
                <v-btn @click="addNewRow" @class="contrast-primary-text" small color="primary">
                    <v-icon small style="margin-left: -5px;">mdi-plus</v-icon>등록
                </v-btn>
                <v-btn :disabled="!selectedRow" style="margin-left: 5px;" @click="openEditDialog()" class="contrast-primary-text" small color="primary">
                    <v-icon small>mdi-pencil</v-icon>수정
                </v-btn>
                <v-btn :disabled="!selectedRow" style="margin-left: 5px;" @click="changeSubscriptionDialog = true" class="contrast-primary-text" small color="primary" :disabled="!hasRole('System')">
                    <v-icon small>mdi-minus-circle-outline</v-icon>구독 상태 변경
                </v-btn>
                <v-dialog v-model="changeSubscriptionDialog" width="500">
                    <ChangeSubscription
                        @closeDialog="changeSubscriptionDialog = false"
                        @changeSubscription="changeSubscription"
                    ></ChangeSubscription>
                </v-dialog>
                <v-btn style="margin-left: 5px;" @click="requestSubscriptionDialog = true" class="contrast-primary-text" small color="primary" :disabled="!hasRole('User')">
                    <v-icon small>mdi-minus-circle-outline</v-icon>구독 신청
                </v-btn>
                <v-dialog v-model="requestSubscriptionDialog" width="500">
                    <RequestSubscription
                        @closeDialog="requestSubscriptionDialog = false"
                        @requestSubscription="requestSubscription"
                    ></RequestSubscription>
                </v-dialog>
                <v-btn :disabled="!selectedRow" style="margin-left: 5px;" @click="cancelSubscriptionDialog = true" class="contrast-primary-text" small color="primary" :disabled="!hasRole('User')">
                    <v-icon small>mdi-minus-circle-outline</v-icon>구독 취소
                </v-btn>
                <v-dialog v-model="cancelSubscriptionDialog" width="500">
                    <CancelSubscription
                        @closeDialog="cancelSubscriptionDialog = false"
                        @cancelSubscription="cancelSubscription"
                    ></CancelSubscription>
                </v-dialog>
            </div>
            <div class="mb-5 text-lg font-bold"></div>
            <div class="table-responsive">
                <v-table>
                    <thead>
                        <tr>
                        <th>Id</th>
                        <th>UserId</th>
                        <th>StartDate</th>
                        <th>EndDate</th>
                        <th>IsSubscription</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="(val, idx) in value" 
                            @click="changeSelectedRow(val)"
                            :key="val"  
                            :style="val === selectedRow ? 'background-color: rgb(var(--v-theme-primary), 0.2) !important;':''"
                        >
                            <td class="font-semibold">{{ idx + 1 }}</td>
                            <td class="whitespace-nowrap" label="UserId">{{ val.userId }}</td>
                            <td class="whitespace-nowrap" label="StartDate">{{ val.startDate }}</td>
                            <td class="whitespace-nowrap" label="EndDate">{{ val.endDate }}</td>
                            <td class="whitespace-nowrap" label="IsSubscription">{{ val.isSubscription }}</td>
                            <v-row class="ma-0 pa-4 align-center">
                                <v-spacer></v-spacer>
                                <Icon style="cursor: pointer;" icon="mi:delete" @click="deleteRow(val)" />
                            </v-row>
                        </tr>
                    </tbody>
                </v-table>
            </div>
        </div>
        <v-col>
            <v-dialog
                v-model="openDialog"
                transition="dialog-bottom-transition"
                width="35%"
            >
                <v-card>
                    <v-toolbar
                        color="primary"
                        class="elevation-0 pa-4"
                        height="50px"
                    >
                        <div style="color:white; font-size:17px; font-weight:700;">Subscription 등록</div>
                        <v-spacer></v-spacer>
                        <v-icon
                            color="white"
                            small
                            @click="closeDialog()"
                        >mdi-close</v-icon>
                    </v-toolbar>
                    <v-card-text>
                        <Subscription :offline="offline"
                            :isNew="!value.idx"
                            :editMode="true"
                            :inList="false"
                            v-model="newValue"
                            @add="append"
                        />
                    </v-card-text>
                </v-card>
            </v-dialog>
            <v-dialog
                v-model="editDialog"
                transition="dialog-bottom-transition"
                width="35%"
            >
                <v-card>
                    <v-toolbar
                        color="primary"
                        class="elevation-0 pa-4"
                        height="50px"
                    >
                        <div style="color:white; font-size:17px; font-weight:700;">Subscription 수정</div>
                        <v-spacer></v-spacer>
                        <v-icon
                            color="white"
                            small
                            @click="closeDialog()"
                        >mdi-close</v-icon>
                    </v-toolbar>
                    <v-card-text>
                        <div>
                            <Number label="SubscriptionId" v-model="selectedRow.subscriptionId" :editMode="true"/>
                            <Number label="UserId" v-model="selectedRow.userId" :editMode="true"/>
                            <Date label="StartDate" v-model="selectedRow.startDate" :editMode="true"/>
                            <Date label="EndDate" v-model="selectedRow.endDate" :editMode="true"/>
                            <Boolean label="IsSubscription" v-model="selectedRow.isSubscription" :editMode="true"/>
                            <v-divider class="border-opacity-100 my-divider"></v-divider>
                            <v-layout row justify-end>
                                <v-btn
                                    width="64px"
                                    color="primary"
                                    @click="save"
                                >
                                    수정
                                </v-btn>
                            </v-layout>
                        </div>
                    </v-card-text>
                </v-card>
            </v-dialog>
        </v-col>
    </v-container>
</template>

<script>
import { ref } from 'vue';
import { useTheme } from 'vuetify';
import BaseGrid from '../base-ui/BaseGrid.vue'


export default {
    name: 'subscriptionGrid',
    mixins:[BaseGrid],
    components:{
    },
    data: () => ({
        path: 'subscriptions',
        changeSubscriptionDialog: false,
        requestSubscriptionDialog: false,
        cancelSubscriptionDialog: false,
    }),
    watch: {
    },
    methods:{
        async changeSubscription(params){
            try{
                var path = "changeSubscription".toLowerCase();
                var temp = await this.repository.invoke(this.selectedRow, path, params)
                // 스넥바 관련 수정 필요
                // this.$EventBus.$emit('show-success','ChangeSubscription 성공적으로 처리되었습니다.')
                for(var i = 0; i< this.value.length; i++){
                    if(this.value[i] == this.selectedRow){
                        this.value[i] = temp.data
                    }
                }
                this.changeSubscriptionDialog = false
            }catch(e){
                console.log(e)
            }
        },
        async requestSubscription(params){
            try{
                var path = "requestSubscription".toLowerCase();
                var temp = await this.repository.invoke(this.selectedRow, path, params)
                // 스넥바 관련 수정 필요
                // this.$EventBus.$emit('show-success','RequestSubscription 성공적으로 처리되었습니다.')
                for(var i = 0; i< this.value.length; i++){
                    if(this.value[i] == this.selectedRow){
                        this.value[i] = temp.data
                    }
                }
                this.requestSubscriptionDialog = false
            }catch(e){
                console.log(e)
            }
        },
        async cancelSubscription(params){
            try{
                var path = "cancelSubscription".toLowerCase();
                var temp = await this.repository.invoke(this.selectedRow, path, params)
                // 스넥바 관련 수정 필요
                // this.$EventBus.$emit('show-success','CancelSubscription 성공적으로 처리되었습니다.')
                for(var i = 0; i< this.value.length; i++){
                    if(this.value[i] == this.selectedRow){
                        this.value[i] = temp.data
                    }
                }
                this.cancelSubscriptionDialog = false
            }catch(e){
                console.log(e)
            }
        },
    }
}

</script>