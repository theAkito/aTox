// SPDX-FileCopyrightText: 2019-2021 Robin Lindén
//
// SPDX-License-Identifier: GPL-3.0-only

package ltd.evilcorp.domain.feature

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ltd.evilcorp.core.repository.ContactRepository
import ltd.evilcorp.core.repository.FriendRequestRepository
import ltd.evilcorp.core.vo.Contact
import ltd.evilcorp.core.vo.FriendRequest
import ltd.evilcorp.domain.tox.PublicKey
import ltd.evilcorp.domain.tox.Tox
import java.util.Date
import javax.inject.Inject

class FriendRequestManager @Inject constructor(
    private val scope: CoroutineScope,
    private val contactRepository: ContactRepository,
    private val friendRequestRepository: FriendRequestRepository,
    private val tox: Tox,
) {
    fun getAll(): Flow<List<FriendRequest>> = friendRequestRepository.getAll()
    fun get(id: PublicKey): Flow<FriendRequest> = friendRequestRepository.get(id.string())

    fun accept(friendRequest: FriendRequest) = scope.launch {
        tox.acceptFriendRequest(PublicKey(friendRequest.publicKey))
        contactRepository.add(Contact(friendRequest.publicKey))
        contactRepository.setLastMessage(friendRequest.publicKey, Date().time)
        friendRequestRepository.delete(friendRequest)
    }

    fun reject(friendRequest: FriendRequest) = scope.launch { friendRequestRepository.delete(friendRequest) }
}
