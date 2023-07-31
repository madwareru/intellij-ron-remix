enum MyRonType {
    A(a::MyRonEnum),
}

mod a {
  enum MyRonEnum {
    <ref>MyRonVariant,
  }
}

mod b {
  enum OtherEnum {
    MyRonVariant,
  }
}