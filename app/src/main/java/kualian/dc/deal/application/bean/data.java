package kualian.dc.deal.application.bean;

import java.util.List;

/**
 * Created by admin on 2018/3/30.
 */

public class data {

    /**
     * is_virtual : false
     * created_time : 1970-01-01T00:00:00
     * is_market : false
     * is_confirmed : false
     * fee : {"amount":100,"asset_id":0}
     * index : 0
     * ledger_entries : [{"amount":{"amount":1000,"asset_id":0},"from_account":"C74yL6JdAJEuNpCC3SD4eprf2Epp8LxgUsYE16mHM2DrpJUgJgM","memo":"qq"}]
     * received_time : 1970-01-01T00:00:00
     * block_num : 0
     * trx_data : 8b28be5a00000000000000000000000301221ca05e32168dff0c67fd19f84eb61f49e90812cbc73da4b8824c0400000000000000022ee80300000000000000000000000000000001001a1c7f6b26a38ee0f74a3fac65845d320252df27727ee3301065004203027171000000000000000000000000000000000000000000
     * extra_addresses : ["CU5ckP4E4yLQe5Yjn7uFHzgabb36o1JxcL"]
     * trx : {"sub_inport_asset":{"amount":0,"asset_id":0},"result_trx_id":"0000000000000000000000000000000000000000","operations":[{"data":{"balance_id":"CX5qXZcWDkZHEzVZpj6kKLtZoRejFMATww","claim_input_data":"","amount":1100},"type":"withdraw_op_type"},{"data":{"amount":1000,"condition":{"slate_id":0,"data":{"owner":"CU5ckP4E4yLQe5Yjn7uFHzgabb36o1JxcL"},"balance_type":"withdraw_common_type","asset_id":0,"type":"withdraw_signature_type"}},"type":"deposit_op_type"},{"data":{"imessage":"qq"},"type":"imessage_memo_op_type"}],"expiration":"2018-03-30T12:07:39","sub_account":"","result_trx_type":"origin_transaction","signatures":[]}
     * entry_id : 0000000000000000000000000000000000000000
     */

    private boolean is_virtual;
    private String created_time;
    private boolean is_market;
    private boolean is_confirmed;
    private FeeBean fee;
    private int index;
    private String received_time;
    private int block_num;
    private String trx_data;
    private TrxBean trx;
    private String entry_id;
    private List<LedgerEntriesBean> ledger_entries;
    private List<String> extra_addresses;

    public boolean isIs_virtual() {
        return is_virtual;
    }

    public void setIs_virtual(boolean is_virtual) {
        this.is_virtual = is_virtual;
    }

    public String getCreated_time() {
        return created_time;
    }

    public void setCreated_time(String created_time) {
        this.created_time = created_time;
    }

    public boolean isIs_market() {
        return is_market;
    }

    public void setIs_market(boolean is_market) {
        this.is_market = is_market;
    }

    public boolean isIs_confirmed() {
        return is_confirmed;
    }

    public void setIs_confirmed(boolean is_confirmed) {
        this.is_confirmed = is_confirmed;
    }

    public FeeBean getFee() {
        return fee;
    }

    public void setFee(FeeBean fee) {
        this.fee = fee;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getReceived_time() {
        return received_time;
    }

    public void setReceived_time(String received_time) {
        this.received_time = received_time;
    }

    public int getBlock_num() {
        return block_num;
    }

    public void setBlock_num(int block_num) {
        this.block_num = block_num;
    }

    public String getTrx_data() {
        return trx_data;
    }

    public void setTrx_data(String trx_data) {
        this.trx_data = trx_data;
    }

    public TrxBean getTrx() {
        return trx;
    }

    public void setTrx(TrxBean trx) {
        this.trx = trx;
    }

    public String getEntry_id() {
        return entry_id;
    }

    public void setEntry_id(String entry_id) {
        this.entry_id = entry_id;
    }

    public List<LedgerEntriesBean> getLedger_entries() {
        return ledger_entries;
    }

    public void setLedger_entries(List<LedgerEntriesBean> ledger_entries) {
        this.ledger_entries = ledger_entries;
    }

    public List<String> getExtra_addresses() {
        return extra_addresses;
    }

    public void setExtra_addresses(List<String> extra_addresses) {
        this.extra_addresses = extra_addresses;
    }

    public static class FeeBean {
        /**
         * amount : 100
         * asset_id : 0
         */

        private int amount;
        private int asset_id;

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public int getAsset_id() {
            return asset_id;
        }

        public void setAsset_id(int asset_id) {
            this.asset_id = asset_id;
        }
    }

    public static class TrxBean {
        /**
         * sub_inport_asset : {"amount":0,"asset_id":0}
         * result_trx_id : 0000000000000000000000000000000000000000
         * operations : [{"data":{"balance_id":"CX5qXZcWDkZHEzVZpj6kKLtZoRejFMATww","claim_input_data":"","amount":1100},"type":"withdraw_op_type"},{"data":{"amount":1000,"condition":{"slate_id":0,"data":{"owner":"CU5ckP4E4yLQe5Yjn7uFHzgabb36o1JxcL"},"balance_type":"withdraw_common_type","asset_id":0,"type":"withdraw_signature_type"}},"type":"deposit_op_type"},{"data":{"imessage":"qq"},"type":"imessage_memo_op_type"}]
         * expiration : 2018-03-30T12:07:39
         * sub_account :
         * result_trx_type : origin_transaction
         * signatures : []
         */

        private SubInportAssetBean sub_inport_asset;
        private String result_trx_id;
        private String expiration;
        private String sub_account;
        private String result_trx_type;
        private List<OperationsBean> operations;
        private List<?> signatures;

        public SubInportAssetBean getSub_inport_asset() {
            return sub_inport_asset;
        }

        public void setSub_inport_asset(SubInportAssetBean sub_inport_asset) {
            this.sub_inport_asset = sub_inport_asset;
        }

        public String getResult_trx_id() {
            return result_trx_id;
        }

        public void setResult_trx_id(String result_trx_id) {
            this.result_trx_id = result_trx_id;
        }

        public String getExpiration() {
            return expiration;
        }

        public void setExpiration(String expiration) {
            this.expiration = expiration;
        }

        public String getSub_account() {
            return sub_account;
        }

        public void setSub_account(String sub_account) {
            this.sub_account = sub_account;
        }

        public String getResult_trx_type() {
            return result_trx_type;
        }

        public void setResult_trx_type(String result_trx_type) {
            this.result_trx_type = result_trx_type;
        }

        public List<OperationsBean> getOperations() {
            return operations;
        }

        public void setOperations(List<OperationsBean> operations) {
            this.operations = operations;
        }

        public List<?> getSignatures() {
            return signatures;
        }

        public void setSignatures(List<?> signatures) {
            this.signatures = signatures;
        }

        public static class SubInportAssetBean {
            /**
             * amount : 0
             * asset_id : 0
             */

            private int amount;
            private int asset_id;

            public int getAmount() {
                return amount;
            }

            public void setAmount(int amount) {
                this.amount = amount;
            }

            public int getAsset_id() {
                return asset_id;
            }

            public void setAsset_id(int asset_id) {
                this.asset_id = asset_id;
            }
        }

        public static class OperationsBean {
            /**
             * data : {"balance_id":"CX5qXZcWDkZHEzVZpj6kKLtZoRejFMATww","claim_input_data":"","amount":1100}
             * type : withdraw_op_type
             */

            private DataBean data;
            private String type;

            public DataBean getData() {
                return data;
            }

            public void setData(DataBean data) {
                this.data = data;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public static class DataBean {
                /**
                 * balance_id : CX5qXZcWDkZHEzVZpj6kKLtZoRejFMATww
                 * claim_input_data :
                 * amount : 1100
                 */

                private String balance_id;
                private String claim_input_data;
                private int amount;
                private condition condition;
                private String imessage;

                public String getBalance_id() {
                    return balance_id;
                }

                public void setBalance_id(String balance_id) {
                    this.balance_id = balance_id;
                }

                public String getClaim_input_data() {
                    return claim_input_data;
                }

                public void setClaim_input_data(String claim_input_data) {
                    this.claim_input_data = claim_input_data;
                }

                public int getAmount() {
                    return amount;
                }

                public void setAmount(int amount) {
                    this.amount = amount;
                }
                public class condition{

                    /**
                     * slate_id : 0
                     * data : {}
                     * balance_type : withdraw_common_type
                     * asset_id : 0
                     * type : withdraw_signature_type
                     */

                    private int slate_id;
                    private DataBe data;
                    private String balance_type;
                    private int asset_id;
                    private String type;

                    public int getSlate_id() {
                        return slate_id;
                    }

                    public void setSlate_id(int slate_id) {
                        this.slate_id = slate_id;
                    }

                    public DataBe getData() {
                        return data;
                    }

                    public void setData(DataBe data) {
                        this.data = data;
                    }

                    public String getBalance_type() {
                        return balance_type;
                    }

                    public void setBalance_type(String balance_type) {
                        this.balance_type = balance_type;
                    }

                    public int getAsset_id() {
                        return asset_id;
                    }

                    public void setAsset_id(int asset_id) {
                        this.asset_id = asset_id;
                    }

                    public String getType() {
                        return type;
                    }

                    public void setType(String type) {
                        this.type = type;
                    }

                    public  class DataBe {

                        /**
                         * owner : CU5ckP4E4yLQe5Yjn7uFHzgabb36o1JxcL
                         */

                        private String owner;

                        public String getOwner() {
                            return owner;
                        }

                        public void setOwner(String owner) {
                            this.owner = owner;
                        }
                    }
                }
            }
        }
    }

    public static class LedgerEntriesBean {
        /**
         * amount : {"amount":1000,"asset_id":0}
         * from_account : C74yL6JdAJEuNpCC3SD4eprf2Epp8LxgUsYE16mHM2DrpJUgJgM
         * memo : qq
         */

        private AmountBean amount;
        private String from_account;
        private String memo;

        public AmountBean getAmount() {
            return amount;
        }

        public void setAmount(AmountBean amount) {
            this.amount = amount;
        }

        public String getFrom_account() {
            return from_account;
        }

        public void setFrom_account(String from_account) {
            this.from_account = from_account;
        }

        public String getMemo() {
            return memo;
        }

        public void setMemo(String memo) {
            this.memo = memo;
        }

        public static class AmountBean {
            /**
             * amount : 1000
             * asset_id : 0
             */

            private int amount;
            private int asset_id;

            public int getAmount() {
                return amount;
            }

            public void setAmount(int amount) {
                this.amount = amount;
            }

            public int getAsset_id() {
                return asset_id;
            }

            public void setAsset_id(int asset_id) {
                this.asset_id = asset_id;
            }
        }
    }
}
