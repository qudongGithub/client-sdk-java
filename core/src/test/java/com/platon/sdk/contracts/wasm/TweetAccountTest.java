package com.platon.sdk.contracts.wasm;

import com.platon.rlp.datatypes.WasmAddress;
import org.junit.Test;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class TweetAccountTest extends BaseContractTest {

    private String address = "lax13370uaqqsm5x7sy6gf4qkhpq9jxk50ajxvlkza";

    @Test
    public void deployByT() throws Exception {
        TweetAccount contract = TweetAccount.deploy(web3j, transactionManager, gasProvider, chainId).send();
        System.out.println("address="+contract.getContractAddress());
    }

    @Test
    public void deployByC() throws Exception {
        TweetAccount contract = TweetAccount.deploy(web3j, credentials, gasProvider, chainId).send();
        System.out.println("address="+contract.getContractAddress());
    }

    @Test
    public void isAdmin() throws Exception {
        TweetAccount contract = loadT();
        Boolean result = contract.isAdmin().send();
        assertThat(result, is(Boolean.TRUE));

        contract = loadD();
        result = contract.isAdmin().send();
        assertThat(result, is(Boolean.TRUE));

        System.out.println(result);
    }

    @Test
    public void tweet() throws Exception {
        TweetAccount contract = loadT();

        long value = contract.getNumberOfTweets().send().value.longValue();
        TransactionReceipt transactionReceipt = contract.tweet("t1").send();
        System.out.println("hash = " + transactionReceipt.getTransactionHash());


        contract = loadD();
        transactionReceipt = contract.tweet("t2").send();
        System.out.println("hash = " + transactionReceipt.getTransactionHash());

        long nowValue = contract.getNumberOfTweets().send().value.longValue();

        assertThat(value, is(nowValue - 2));
        System.out.println(value);

    }

    @Test
    public void getOwnerAddress() throws Exception {
        TweetAccount contract = loadT();
        WasmAddress owner = contract.getOwnerAddress().send();
        assertThat(owner.getAddress(), is(credentialsAddress));

        contract = loadD();
        owner = contract.getOwnerAddress().send();
        assertThat(owner.getAddress(), is(credentialsAddress));

        System.out.println(owner.getAddress());
    }


    private TweetAccount loadT(){
        return TweetAccount.load(address, web3j, transactionManager, gasProvider, chainId);
    }

    private TweetAccount loadD(){
        return TweetAccount.load(address, web3j, credentials, gasProvider, chainId);
    }
}
