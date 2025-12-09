#!/bin/bash

# --- Security Best Practices Keystore Generation Script ---

# Variables
SERVER_KEYSTORE="server.p12"
CLIENT1_KEYSTORE="client1.p12"
CLIENT2_KEYSTORE="client2.p12"

SERVER_CERT="server.cer"
CLIENT1_CERT="client1.cer"
CLIENT2_CERT="client2.cer"

PASSWORD="password"
STORE_TYPE="PKCS12"
KEY_ALG="RSA"
KEY_SIZE="2048"

SERVER_ALIAS="server"
CLIENT1_ALIAS="client1"
CLIENT2_ALIAS="client2"

# Use a more realistic Distinguished Name (DNAME)
DNAME_SERVER="CN=localhost, O=Demo, C=ES"
DNAME_CLIENT1="CN=Client1, O=Demo, C=ES"
DNAME_CLIENT2="CN=Client2, O=Demo, C=ES"

# Define the resource directory for the final keystores
RESOURCE_DIR="utilities/src/main/resources"

# --- Start of Operations ---

# 1. Cleanup and Preparation
echo "--- Cleaning up previous artifacts and preparing directory ---"
rm -f *.p12 *.cer
mkdir -p $RESOURCE_DIR

# 2. Generate Server Keystore
echo "--- Generating Server Keystore ($SERVER_KEYSTORE) ---"
keytool -genkeypair \
  -alias $SERVER_ALIAS \
  -keyalg $KEY_ALG \
  -keysize $KEY_SIZE \
  -keystore $SERVER_KEYSTORE \
  -dname "$DNAME_SERVER" \
  -storetype $STORE_TYPE \
  -storepass $PASSWORD \
  -keypass $PASSWORD

# 3. Generate Client1 Keystore
echo "--- Generating Client1 Keystore ($CLIENT1_KEYSTORE) ---"
keytool -genkeypair \
  -alias $CLIENT1_ALIAS \
  -keyalg $KEY_ALG \
  -keysize $KEY_SIZE \
  -keystore $CLIENT1_KEYSTORE \
  -dname "$DNAME_CLIENT1" \
  -storetype $STORE_TYPE \
  -storepass $PASSWORD \
  -keypass $PASSWORD

# 4. Generate Client2 Keystore
echo "--- Generating Client2 Keystore ($CLIENT2_KEYSTORE) ---"
keytool -genkeypair \
  -alias $CLIENT2_ALIAS \
  -keyalg $KEY_ALG \
  -keysize $KEY_SIZE \
  -keystore $CLIENT2_KEYSTORE \
  -dname "$DNAME_CLIENT2" \
  -storetype $STORE_TYPE \
  -storepass $PASSWORD \
  -keypass $PASSWORD

# 5. Export Public Certificates for Trust Exchange
echo "--- Exporting public certificates ---"
keytool -exportcert -alias $SERVER_ALIAS -keystore $SERVER_KEYSTORE -file $SERVER_CERT -storepass $PASSWORD
keytool -exportcert -alias $CLIENT1_ALIAS -keystore $CLIENT1_KEYSTORE -file $CLIENT1_CERT -storepass $PASSWORD
keytool -exportcert -alias $CLIENT2_ALIAS -keystore $CLIENT2_KEYSTORE -file $CLIENT2_CERT -storepass $PASSWORD

# 6. Establish Trust: Import certificates into respective keystores
echo "--- Importing client certificates into Server Keystore (for client authentication) ---"
keytool -importcert -alias $CLIENT1_ALIAS -keystore $SERVER_KEYSTORE -file $CLIENT1_CERT -storepass $PASSWORD -noprompt
keytool -importcert -alias $CLIENT2_ALIAS -keystore $SERVER_KEYSTORE -file $CLIENT2_CERT -storepass $PASSWORD -noprompt

echo "--- Importing server certificate into Client Keystores (for server authentication) ---"
keytool -importcert -alias $SERVER_ALIAS -keystore $CLIENT1_KEYSTORE -file $SERVER_CERT -storepass $PASSWORD -noprompt
keytool -importcert -alias $SERVER_ALIAS -keystore $CLIENT2_KEYSTORE -file $SERVER_CERT -storepass $PASSWORD -noprompt

# 7. Finalize: Move keystores to the resources folder and clean up
echo "--- Moving keystores to the resources folder: $RESOURCE_DIR ---"
# The server keystore is renamed to match crypto.properties
mv $SERVER_KEYSTORE "$RESOURCE_DIR/keystore.p12"
mv $CLIENT1_KEYSTORE "$RESOURCE_DIR/"
mv $CLIENT2_KEYSTORE "$RESOURCE_DIR/"

echo "--- Cleaning up temporary certificate files ---"
rm -f $SERVER_CERT $CLIENT1_CERT $CLIENT2_CERT

echo "--- Keystore generation complete. Final files in $RESOURCE_DIR: ---"
ls -l $RESOURCE_DIR

echo -e "\nScript finished successfully. Your PKCS12 keystores are ready."
