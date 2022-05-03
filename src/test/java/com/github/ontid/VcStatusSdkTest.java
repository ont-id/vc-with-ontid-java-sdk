package com.github.ontid;

import com.github.ontio.account.Account;
import com.github.ontio.common.Helper;
import com.github.ontio.crypto.SignatureScheme;
import com.github.ontio.ontid.OntIdPubKey;
import com.github.ontio.sdk.exception.SDKException;
import junit.framework.TestCase;

public class VcStatusSdkTest extends TestCase {

    //AYU66Y6eckqPPUGvt2AA2vNdNxJdeXew5y
    String did2 = "did:ont:AYU66Y6eckqPPUGvt2AA2vNdNxJdeXew5y";
    Account signer = new Account(Helper.hexToBytes("6321d9880fccb9f5ff7063d840ee4f2f4d8158736b7a4d9fe67628fd70dc000e"), SignatureScheme.SHA256WITHECDSA);

    //AdUgHDJZGiUWsQZHbJ4K7rEyLwbYWSrJ2B
    //did:ont:AdUgHDJZGiUWsQZHbJ4K7rEyLwbYWSrJ2B
    String did = "did:ont:AdUgHDJZGiUWsQZHbJ4K7rEyLwbYWSrJ2B";
    Account acc = new Account(Helper.hexToBytes("d4716f050504e711c5bd2a3f2f5f518a9e811f5d0c46aebdf0c81f5ce85ff805"), SignatureScheme.SHA256WITHECDSA);

    VcStatusSdk vs = new VcStatusSdk("http://polaris1.ont.io:20336");
    String contractAddr = "52df370680de17bc5d4262c446f102a0ee0d6312";

    String vcId = "1";
    String vcId2 = "2";
    long gasLimit = 20000;
    long gasPrice = 2500;

    public VcStatusSdkTest() throws Exception {
        vs.setCredentialRecordContractAddress(contractAddr);
    }

    public void testGetPublicKeyFromDID() throws Exception {
//        vs.ontSdk.nativevm().ontId().sendAddPubKey(did, acc, signer.serializePublicKey(), did2, acc, 20000, 2500);
        OntIdPubKey pk = vs.getPublicKeyFromDID(did, "did:ont:AdUgHDJZGiUWsQZHbJ4K7rEyLwbYWSrJ2B#keys-1");
        assertEquals(did + "#keys-1", pk.id);
    }

    public void testGetPublicKeyIndexFromDID() throws SDKException {
        int index = vs.getPublicKeyIndexFromDID(did, "did:ont:AdUgHDJZGiUWsQZHbJ4K7rEyLwbYWSrJ2B#keys-1");
        System.out.println(index);
    }

    public void testRegisterVCOnChain() throws Exception {
        String txHash = vs.registerVCOnChain(contractAddr, vcId2, did, did2, gasPrice, gasLimit, signer, signer);
        System.out.println(txHash);
    }


    public void testRevokeVCOnChain() throws Exception {
        String txHash = vs.revokeVCOnChain(contractAddr, vcId2, did, gasPrice, gasLimit, acc, signer);
        System.out.println(txHash);
    }

    public void testGetVCStatusOnChain() throws Exception {
        String status = vs.getVCStatusOnChain(contractAddr, vcId2);
        System.out.println("status:" + status);
    }
}