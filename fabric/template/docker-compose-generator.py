#!/usr/bin/python3
# -*- coding:utf-8 -*-

import yaml
import os

FABRIC_IMAGE_TAG = 'x86_64-1.1.0'
COUCHDB_IMAGE_TAG = 'x86_64-0.4.6'

# Environment Flag set by Demo, if exist and set, containers are running all together on one machine
CTNR_IS_LOCAL = os.environ.get('FABRIC_LOCAL_CTNR', 'NO') == 'YES'
print('CTNR_IS_LOCAL is {0}'.format(CTNR_IS_LOCAL))


def generate_cli_config(org_domain, org_msp, org_name):
    ''' generate docker compose yaml configuration for cli

    '''
    cfg = dict()
    cfg['version'] = '2'
    cfg['networks'] = dict()
    cfg['services'] = dict()

    # networks
    if CTNR_IS_LOCAL:
        cfg['networks']['byfn'] = None
    else:
        cfg['networks']['byfn'] = dict()
        cfg['networks']['byfn']['ipam'] = dict()
        cfg['networks']['byfn']['ipam']['driver'] = 'default'
        cfg['networks']['byfn']['ipam']['config'] = [
            {'subnet': '172.18.0.0/24'}]
        cfg['networks']['byfn']['driver'] = 'bridge'

    ser_cli = dict()
    cfg['services']['{0}cli'.format(org_name)] = ser_cli

    ser_cli['container_name'] = '{0}cli'.format(org_name)
    ser_cli['image'] = 'hyperledger/fabric-tools:{0}'.format(FABRIC_IMAGE_TAG)
    ser_cli['tty'] = True
    ser_cli['stdin_open'] = True
    ser_cli['working_dir'] = '/opt/gopath/src/github.com/hyperledger/fabric/peer'
    ser_cli['command'] = '/bin/bash'
    ser_cli['networks'] = ['byfn']

    env = list()
    ser_cli['environment'] = env
    PEER_ORG_DIR = '/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations'
    env.append('GOPATH=/opt/gopath')
    env.append('CORE_VM_ENDPOINT=unix:///host/var/run/docker.sock')
    env.append('CORE_LOGGING_LEVEL=INFO')
    env.append('CORE_PEER_ID={domain}cli'.format(domain=org_name))
    env.append('CORE_PEER_ADDRESS=peer0.{domain}:7051'.format(
        domain=org_domain))
    env.append('CORE_PEER_LOCALMSPID={msp}'.format(msp=org_msp))
    env.append('CORE_PEER_TLS_ENABLED=false')
    env.append('CORE_PEER_TLS_CERT_FILE={peer_org_dir}/{domain}/peers/peer0.{domain}/tls/server.crt'.format(
        peer_org_dir=PEER_ORG_DIR, domain=org_domain))
    env.append('CORE_PEER_TLS_KEY_FILE={peer_org_dir}/{domain}/peers/peer0.{domain}/tls/server.key'.format(
        peer_org_dir=PEER_ORG_DIR, domain=org_domain))
    env.append('CORE_PEER_TLS_ROOTCERT_FILE={peer_org_dir}/{domain}/peers/peer0.{domain}/tls/ca.crt'.format(
        peer_org_dir=PEER_ORG_DIR, domain=org_domain))
    env.append(
        'CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/{0}/users/Admin@{0}/msp'.format(org_domain))

    volumes = list()
    ser_cli['volumes'] = volumes
    volumes.append('/var/run/:/host/var/run/')
    volumes.append(
        './crypto-config:/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/')
    volumes.append(
        './channel-artifacts/:/opt/gopath/src/github.com/hyperledger/fabric/peer/channel-artifacts/')
    volumes.append(
        '../../docker_scripts/:/opt/gopath/src/github.com/hyperledger/fabric/peer/scripts/')
    volumes.append('../../chaincode/:/opt/gopath/src/github.com/chaincode/')

    return cfg


def generate_orderer_config(orderer_domain, orderer_msp, orderer_name, outer_port_7050):
    ''' generate docker compose yaml configuration for orderer

    '''
    cfg = dict()
    cfg['version'] = '2'
    cfg['volumes'] = {orderer_name + '.' + orderer_domain:  None}

    cfg['networks'] = dict()

    # if CTNR_IS_LOCAL:
    cfg['networks']['byfn'] = None

    ser_orderer = dict()
    ser_orderer['container_name'] = '{0}.{1}'.format(
        orderer_name, orderer_domain) if CTNR_IS_LOCAL else 'ctnr-{0}'.format(orderer_name)
    ser_orderer['image'] = 'hyperledger/fabric-orderer:{0}'.format(
        FABRIC_IMAGE_TAG)
    ser_orderer['working_dir'] = '/opt/gopath/src/github.com/hyperledger/fabric/peer'
    ser_orderer['command'] = 'orderer'
    ser_orderer['networks'] = ['byfn']
    ser_orderer['ports'] = ['{0}:7050'.format(outer_port_7050)]

    cfg['services'] = dict()
    cfg['services']['{0}.{1}'.format(
        orderer_name, orderer_domain) if CTNR_IS_LOCAL else 'service_orderer'] = ser_orderer

    env = list()
    ser_orderer['environment'] = env
    env.append('ORDERER_GENERAL_LOGLEVEL=INFO')
    env.append('ORDERER_GENERAL_LISTENADDRESS=0.0.0.0')
    env.append('ORDERER_GENERAL_GENESISMETHOD=file')
    env.append(
        'ORDERER_GENERAL_GENESISFILE=/var/hyperledger/orderer/{orderer_name}.genesis.block'.format(orderer_name=orderer_name))
    env.append('ORDERER_GENERAL_LOCALMSPID={orderer_msp}'.format(
        orderer_msp=orderer_msp))
    env.append('ORDERER_GENERAL_LOCALMSPDIR=/var/hyperledger/orderer/msp')
    env.append('ORDERER_GENERAL_TLS_ENABLED=false')
    env.append(
        'ORDERER_GENERAL_TLS_PRIVATEKEY=/var/hyperledger/orderer/tls/server.key')
    env.append(
        'ORDERER_GENERAL_TLS_CERTIFICATE=/var/hyperledger/orderer/tls/server.crt')
    env.append(
        'ORDERER_GENERAL_TLS_ROOTCAS=[/var/hyperledger/orderer/tls/ca.crt]')

    volumes = list()
    ser_orderer['volumes'] = volumes
    volumes.append(
        './channel-artifacts/genesis.block:/var/hyperledger/orderer/{orderer_name}.genesis.block'.format(orderer_name=orderer_name))
    volumes.append('./crypto-config/ordererOrganizations/{orderer_domain}/orderers/{orderer_name}.{orderer_domain}/msp:/var/hyperledger/orderer/msp'.format(
        orderer_name=orderer_name, orderer_domain=orderer_domain))
    volumes.append('./crypto-config/ordererOrganizations/{orderer_domain}/orderers/{orderer_name}.{orderer_domain}/tls/:/var/hyperledger/orderer/tls'.format(
        orderer_name=orderer_name, orderer_domain=orderer_domain))
    volumes.append('{orderer_name}.{orderer_domain}:/var/hyperledger/production/orderer'.format(
        orderer_name=orderer_name, orderer_domain=orderer_domain))

    return cfg


def generate_peer_config(org_domain, org_msp, org_peer_index, org_peer_outer_7051, org_peer_outer_7053, couchdb_port):
    ''' generate docker compose yaml configuration for peer

    '''
    cfg = dict()
    cfg['version'] = '2'
    cfg['volumes'] = {'peer' + str(org_peer_index) + '.' + org_domain:  None}

    cfg['networks'] = dict()

    if CTNR_IS_LOCAL:
        cfg['networks']['byfn'] = None
    else:
        cfg['networks']['byfn'] = dict()
        cfg['networks']['byfn']['ipam'] = dict()
        cfg['networks']['byfn']['ipam']['driver'] = 'default'
        cfg['networks']['byfn']['ipam']['config'] = [
            {'subnet': '172.18.0.0/24'}]
        cfg['networks']['byfn']['driver'] = 'bridge'

    cfg['services'] = dict()

    # COUCHDB
    couchdb_ctnr_name = 'couchdb{0}.{1}'.format(
        org_peer_index, org_domain) if CTNR_IS_LOCAL else 'ctnr-couchdb{0}'.format(org_peer_index)

    ser_db = dict()
    cfg['services'][couchdb_ctnr_name] = ser_db

    ser_db['container_name'] = couchdb_ctnr_name
    ser_db['image'] = 'hyperledger/fabric-couchdb:{0}'.format(
        COUCHDB_IMAGE_TAG)
    ser_db['networks'] = ['byfn']
    ser_db['ports'] = ['{0}:5984'.format(
        couchdb_port)] if CTNR_IS_LOCAL else []

    env = list()
    ser_db['environment'] = env
    env.append('COUCHDB_USER=')
    env.append('COUCHDB_PASSWORD=')

    hosts = list()
    ser_db['extra_hosts'] = hosts

    # PEER
    ser_peer = dict()
    peer_ctnr_name = 'peer{0}.{1}'.format(
        org_peer_index, org_domain) if CTNR_IS_LOCAL else 'ctnr-peer{0}'.format(org_peer_index)
    cfg['services'][peer_ctnr_name] = ser_peer

    ser_peer['container_name'] = peer_ctnr_name
    ser_peer['image'] = 'hyperledger/fabric-peer:{0}'.format(FABRIC_IMAGE_TAG)
    ser_peer['working_dir'] = '/opt/gopath/src/github.com/hyperledger/fabric/peer'
    ser_peer['depends_on'] = [couchdb_ctnr_name]
    ser_peer['command'] = 'peer node start'

    ser_peer['ports'] = list()
    ser_peer['ports'].append('{0}:7051'.format(org_peer_outer_7051))
    ser_peer['ports'].append('{0}:7053'.format(org_peer_outer_7053))

    ser_peer['networks'] = ['byfn']

    env = list()
    ser_peer['environment'] = env

    env.append('CORE_VM_ENDPOINT=unix:///host/var/run/docker.sock')
    env.append('CORE_VM_DOCKER_HOSTCONFIG_NETWORKMODE=net_byfn')
    env.append('CORE_LOGGING_LEVEL=INFO')
    env.append('CORE_PEER_GOSSIP_USELEADERELECTION=true')
    env.append('CORE_PEER_GOSSIP_ORGLEADER=false')
    env.append('CORE_PEER_PROFILE_ENABLED=true')
    env.append('CORE_PEER_TLS_ENABLED=false')
    env.append('CORE_PEER_TLS_CERT_FILE=/etc/hyperledger/fabric/tls/server.crt')
    env.append('CORE_PEER_TLS_KEY_FILE=/etc/hyperledger/fabric/tls/server.key')
    env.append('CORE_PEER_TLS_ROOTCERT_FILE=/etc/hyperledger/fabric/tls/ca.crt')
    env.append('CORE_PEER_ID=peer{0}.{1}'.format(org_peer_index, org_domain))
    env.append('CORE_PEER_ADDRESS=peer{0}.{1}:7051'.format(
        org_peer_index, org_domain))
    env.append('CORE_PEER_GOSSIP_EXTERNALENDPOINT=peer{0}.{1}:7051'.format(
        org_peer_index, org_domain))
    env.append('CORE_PEER_LOCALMSPID={0}'.format(org_msp))
    env.append('CORE_LEDGER_STATE_STATEDATABASE=CouchDB')
    env.append('CORE_LEDGER_STATE_COUCHDBCONFIG_COUCHDBADDRESS={couchdb}:5984'.format(
        couchdb=couchdb_ctnr_name))
    env.append('CORE_LEDGER_STATE_COUCHDBCONFIG_USERNAME=')
    env.append('CORE_LEDGER_STATE_COUCHDBCONFIG_PASSWORD=')

    if int(org_peer_index) >= 1:
        env.append(
            'CORE_PEER_GOSSIP_BOOTSTRAP=peer0.{0}:7051'.format(org_domain))

    volumes = list()
    ser_peer['volumes'] = volumes
    volumes.append('/var/run/:/host/var/run/')
    volumes.append('./crypto-config/peerOrganizations/{org_domain}/peers/peer{org_peer_index}.{org_domain}/msp:/etc/hyperledger/fabric/msp'.format(
        org_peer_index=org_peer_index, org_domain=org_domain))
    volumes.append('./crypto-config/peerOrganizations/{org_domain}/peers/peer{org_peer_index}.{org_domain}/tls:/etc/hyperledger/fabric/tls'.format(
        org_peer_index=org_peer_index, org_domain=org_domain))
    volumes.append('peer{org_peer_index}.{org_domain}:/var/hyperledger/production'.format(
        org_peer_index=org_peer_index, org_domain=org_domain))

    return cfg


if __name__ == '__main__':
    import argparse
    parser = argparse.ArgumentParser(
        description='Generate Docker-Compose YAML config')

    parser.add_argument('-t', '--type', dest='TYPE', action='store', choices={
                        'cli', 'orderer', 'peer'}, help='type of docker compose yaml to generate')
    parser.add_argument('-d', '--domain', dest='DOMAIN',
                        action='store', help='Organization Domain')
    parser.add_argument('-n', '--name', dest='NAME',
                        action='store', help='Organization Name')
    parser.add_argument('-m', '--msp', dest='MSP',
                        action='store', help='Organization MSP')

    parser.add_argument('-i', '--index', dest='INDEX',
                        action='store', help='Organization Peer Index')

    parser.add_argument('--port-7050', dest='PORT_7050', action='store',
                        help='host port for peer container port 7051')
    parser.add_argument('--port-7051', dest='PORT_7051', action='store',
                        help='host port for peer container port 7051')
    parser.add_argument('--port-7052', dest='PORT_7052', action='store',
                        help='host port for peer container port 7052')
    parser.add_argument('--port-7053', dest='PORT_7053', action='store',
                        help='host port for peer container port 7053')
    parser.add_argument('--port-couchdb', dest='PORT_COUCHDB',
                        action='store', help='host port for couchdb container')

    parser.add_argument('-o', '--output', dest='OUT_PUT',
                        action='store', help='output file')

    args = parser.parse_args()

    cfg = None

    if args.TYPE is None or args.DOMAIN is None or args.NAME is None or args.MSP is None or args.OUT_PUT is None:
        print(args.TYPE)
        print(args.DOMAIN)
        print(args.NAME)
        print(args.MSP)
        print(args.OUT_PUT)

        parser.print_help()
    elif args.TYPE == 'cli':
        cfg = generate_cli_config(args.DOMAIN, args.MSP, args.NAME)
    elif args.TYPE == 'orderer':
        cfg = generate_orderer_config(
            args.DOMAIN, args.MSP, args.NAME, args.PORT_7050)
    elif args.TYPE == 'peer':
        cfg = generate_peer_config(
            args.DOMAIN, args.MSP, args.INDEX, args.PORT_7051, args.PORT_7053, args.PORT_COUCHDB)
    else:
        print('Boom!Boom!Boom!')
        exit(1)
        pass

    yaml_content = yaml.dump(cfg, default_flow_style=False)
    with open(args.OUT_PUT, 'w') as f:
        f.write(yaml_content)
