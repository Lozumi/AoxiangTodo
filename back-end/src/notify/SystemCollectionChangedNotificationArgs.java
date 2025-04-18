package notify;

public class SystemCollectionChangedNotificationArgs {
    CollectionOperationType operationType = CollectionOperationType.NotIndicated;

    Integer addedItemInnerId, removedItemInnerId, modifiedItemInnerId;

    public CollectionOperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(CollectionOperationType operationType) {
        this.operationType = operationType;
    }

    public Integer getAddedItemInnerId() {
        return addedItemInnerId;
    }

    public void setAddedItemInnerId(Integer addedItemInnerId) {
        this.addedItemInnerId = addedItemInnerId;
    }

    public Integer getRemovedItemInnerId() {
        return removedItemInnerId;
    }

    public void setRemovedItemInnerId(Integer removedItemInnerId) {
        this.removedItemInnerId = removedItemInnerId;
    }

    public Integer getModifiedItemInnerId() {
        return modifiedItemInnerId;
    }

    public void setModifiedItemInnerId(Integer modifiedItemInnerId) {
        this.modifiedItemInnerId = modifiedItemInnerId;
    }
}
