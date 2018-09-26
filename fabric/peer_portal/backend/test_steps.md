source ./scripts/init.sh;init_digest 'digest' 'digest.com' '7051' '5984'

source ./scripts/init.sh;init_adv 'adv1' 'adv1.com' '7151' '6084'

source ./scripts/init.sh;init_affi 'affi1' 'affi1.com' '7251' '6184'


source ./scripts/deploy.sh;deploy_digest 'digest' 'digest.com'

source ./scripts/deploy.sh;deploy_adv 'adv1' 'adv1.com'

source ./scripts/deploy.sh;deploy_affi 'affi1' 'affi1.com'

source ./scripts/channel.sh;create adv1 adv1attributecc 'adv1.com' 'attributecc'

source ./scripts/channel.sh;add_org adv1 'adv1.com' adv1attributecc 'affi1' 'affi1.com' 'attributecc'


cd ~/Programming/fabric/execute/execution/peer_portal/backend/