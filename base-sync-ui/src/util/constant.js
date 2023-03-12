import store from '../store/index'
export default {
    methods:{

        getConst(group) {

            if (!group) {
                return [];
            }

            const arr = store.state.constant.filter(e => e.group === group);
            if (arr.length) {
                return arr[0].itemList;
            }
            return [];
        },

        formatConst(list) {

            const obj = {};
            list.forEach((ele) => {

                obj[ele.code] = ele.name;
            })

            return obj;
        }
    }
}