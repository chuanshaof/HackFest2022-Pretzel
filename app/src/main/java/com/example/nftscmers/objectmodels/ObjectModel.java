package com.example.nftscmers.objectmodels;

import java.io.Serializable;

public interface ObjectModel extends Serializable {
    String getDocumentId();

    void setDocumentId(String documentId);
}
