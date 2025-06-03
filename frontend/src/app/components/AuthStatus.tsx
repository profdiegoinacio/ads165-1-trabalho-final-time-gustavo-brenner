'use client';

import { useSession, signIn, signOut } from 'next-auth/react';

export default function AuthStatus() {
    const { data: session, status } = useSession();

    if (status === 'loading') {
        return <p>Loading...</p>;
    }

    if (session) {
        return (
            <>
                <p>Signed in as {session.user?.username || session.user?.email}</p>
                {session.user?.roles && <p>Roles: {session.user.roles.join(', ')}</p>}
                <button onClick={() => signOut({ callbackUrl: '/' })}>Sign out</button>
            </>
        );
    }

    return (
        <>
            <p>Not signed in</p>
            <button onClick={() => signIn()}>Sign in</button>
        </>
    );
}