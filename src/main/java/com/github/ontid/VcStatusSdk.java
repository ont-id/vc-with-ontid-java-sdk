package com.github.ontid;

import com.alibaba.fastjson.JSON;
import com.github.ontio.OntSdk;
import com.github.ontio.account.Account;
import com.github.ontio.common.Helper;
import com.github.ontio.core.transaction.Transaction;
import com.github.ontio.ontid.OntIdPubKey;
import com.github.ontio.sdk.exception.SDKException;

import java.util.ArrayList;

public class VcStatusSdk {
    OntSdk ontSdk;

    public VcStatusSdk(String rpcAddr) {
        ontSdk = OntSdk.getInstance();
        ontSdk.setRpc(rpcAddr);
    }

    public void setCredentialRecordContractAddress(String contractAddr) {
        this.ontSdk.neovm().credentialRecord().setContractAddress(contractAddr);
    }

    // @title getPublicKeyFromDID
    // @description	Get the specific public key from a DID
    // @param     did	String	    "a DID"
    // @param     keyId	String	    "the verification method identifier"
    // @return    a public key
    public OntIdPubKey getPublicKeyFromDID(String did, String keyId) throws SDKException {
        ArrayList<OntIdPubKey> publicKeyList = this.getPublicKeyListByOntID(did);
        for (OntIdPubKey pk : publicKeyList) {
            if (pk.id.equals(keyId)) {
                return pk;
            }
        }
        throw new SDKException("GetPublicKeyId, record not found");
    }

    public int getPublicKeyIndexFromDID(String did, String publicKeyHex) throws SDKException {
        ArrayList<OntIdPubKey> publicKeyList = this.getPublicKeyListByOntID(did);
        int i = 0;
        for (OntIdPubKey pk : publicKeyList) {
            if (pk.publicKeyHex.equals(publicKeyHex)) {
                return i + 1;
            }
            i += 1;
        }
        throw new SDKException("GetPublicKeyId, record not found");
    }


    // @title registerVCOnChain
    // @description Register the VC on the blockchain network described in the VC's credentialStatus field and make the on-chain status active. This function can be only called by the issuer.
    //@param contractAddress String "the ont chain did contract addr"
    // @param     vcId	 	 String	    "the identifier of the VC
    // @param     issuerId	 String	    "the identifier of the issuer"
    // @param     holderId	 String	    "the identifier of the holder"
    // @param     gasPrice	 long	    "gas price"
    // @param     gasLimit	 long	    "gas limit"
    // @param     signer	 Account	"the account of the issuer"
    // @param     payer	     Account	"the account of the payer"
    // @return the transaction hash
    public String registerVCOnChain(String contractAddress, String vcId, String issuerId, String holderId,
                                    long gasPrice, long gasLimit, Account signer, Account payer) throws Exception {
        int index = this.getPublicKeyIndexFromDID(issuerId, Helper.toHexString(signer.serializePublicKey()));
        if (index == 0) {
            throw new SDKException("GetPublicKeyId, record not found");
        }
        this.ontSdk.neovm().credentialRecord().setContractAddress(contractAddress);
        Transaction tx = this.ontSdk.neovm().credentialRecord().makeCommit2(issuerId, holderId, vcId, index, payer.getAddressU160().toBase58(), gasLimit, gasPrice);
        this.ontSdk.addSign(tx, signer);
        this.ontSdk.addSign(tx, payer);
        this.ontSdk.getConnect().sendRawTransaction(tx);
        return tx.hash().toHexString();
    }


    public String revokeVCOnChain(String contractAddress, String vcId, String issuerId, long gasPrice, long gasLimit,
                                  Account signer, Account payer) throws Exception {

        int index = this.getPublicKeyIndexFromDID(issuerId, Helper.toHexString(signer.serializePublicKey()));
        this.ontSdk.neovm().credentialRecord().setContractAddress(contractAddress);
        Transaction tx = this.ontSdk.neovm().credentialRecord().makeRevoke2(issuerId, vcId, index, payer.getAddressU160().toBase58(), gasLimit, gasPrice);
        this.ontSdk.addSign(tx, signer);
        this.ontSdk.addSign(tx, payer);
        this.ontSdk.getConnect().sendRawTransaction(tx);
        return tx.hash().toHexString();
    }

    // @title CommitVCStatusToChain
    // @description Revoke the VC on the blockchain network described in the VC's credentialStatus field and make the on-chain status revoked.
    //@param contractAddress String "the ont chain did contract addr"
    // @param     vcId	String	    "a verifiable credential to be checked"
    // @return    the VC's on-chain status
    public String getVCStatusOnChain(String contractAddress, String vcId) throws Exception {
        this.ontSdk.neovm().credentialRecord().setContractAddress(contractAddress);
        return this.ontSdk.neovm().credentialRecord().sendGetStatus2(vcId);
    }

    private ArrayList<OntIdPubKey> getPublicKeyListByOntID(String did) throws SDKException {
        String allPubKeysJson = null;
        try {
            allPubKeysJson = this.ontSdk.nativevm().ontId().sendGetPublicKeys(did);
        } catch (Exception e) {
            throw new SDKException(e.getMessage());
        }
        return new ArrayList<>(JSON.parseArray(allPubKeysJson, OntIdPubKey.class));
    }
}
