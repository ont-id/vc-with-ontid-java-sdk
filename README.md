# VC With ONT ID Java SDK

This is an SDK, which is written in the Java language, for using ONT ID in a verifiable credentials system.

## ONT ID
ONT ID, which is developed by the Ontology team, is a Web3 identity framework based on the W3C recommendations for decentralized identifiers and verifiable credentials, using blockchain and cryptographic technology, that can quickly identify and connect people, data, and services. It is decentralized, self-sovereign, privacy-preserving, and easy to use, enabling users to fully control their own data and identities.

## Functionalities
This SDK should be used with the VC-JAVA-SDK which implements the main functionalities of a verifiable credential system.  Besides using ONT ID to identify the involved roles in the verifiable credential system, this SDK also supports rerecording the verifiable credential status in the Ontology blockchain. Because the information in a verifiable credential may be changed for some security reason, the statuses of a verifiable credential include “normal” and “revoked” at least.

With the help of this SDK, issuers can change the verifiable credentials from “normal” to “revoked”. It is worth noting that trying the opposite direction will not work due to security concerns. Holders can also revoke their credentials on their own.

## Business Scenario
Let us describe a business scenario. ABC is a vocational training institution that issues verifiable credentials (which can be used as vocational training certificates) to trainees. ABC records the status of this verifiable credential in the Ontology blockchain using this SDK. Alice obtained a verifiable credential from ABC and its status is “normal”. But now Alice suspects that her private key, which is important for generating verifiable presentations, has leaked. Alice then can update his verifiable credentials status and labels it as “revoked”. As a result, verifiers that received verifiable presentations derived from Alice’s verifiable credentials can say these presentations are invalid.
