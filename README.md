# LVM: LuPi13's VeinMiner
### 테라리아 TModloader 하다가 마크 하니까 답답해서 후딱 만듬


## 1. Download
- 오른쪽 Release 항목을 눌러 원하는 버전을 다운로드 하거나, [여기(아직 안만듬)](https://github.com/LuPi13/LIMPI)를 눌러 최신 버전을 다운 받을 수 있게 만들겠읍니다


## 2. Features
- 광물(또는 config.yml의 Whitelist에 등록된 블럭)을 부술 때, 부순 블럭과 동일한 블럭이 붙어있다면 자동으로 같이 파괴됩니다!
- config.yml을 통해 다음 속성들을 변경할 수 있습니다(자세한 사항은 config.yml 참조):
  + PermissionRequired: VeinMining의 접근성을 제어합니다.
  + Ticking: 1틱 당 파괴되는 블럭의 개수입니다.
  + MaxChain: 한 번의 VeinMining이 파괴하는 최대 블럭의 개수입니다.
  + TeleportDrops: VeinMining으로 파괴된 블럭의 아이템이 유저에게 텔레포트됩니다.(미구현)
  + BreakSimilars: 주변의 유사한 블럭도 파괴합니다.
  + BreakDiagonal: 모서리나 꼭짓점만 닿은 블럭도 파괴합니다.
  + OverrideBlacklist: Blacklist에 등록된 블럭을 연쇄파괴하지 못하게 설정합니다.
  + IndividualWhitelist: 연쇄파괴할 아이템 등록을 개인마다 다르게 설정할 수 있게 합니다.(미구현)
  + Whitelist: VeinMining이 작동할 블럭을 설정합니다.
  + Blacklist: VeinMining이 작동하지 않을 블럭을 설정합니다.
  + Similars: BreakSimilars가 true일 때, 어떤 블럭을 유사한 블럭으로 취급할지 설정합니다.

## 3. Changed log
- 1.0: VeinMining 기본 알고리즘 제작 (FIRST COMMIT)